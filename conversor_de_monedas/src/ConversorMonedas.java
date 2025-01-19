// Paso 1: Configuración del Ambiente Java
// Asegúrate de tener Java y un IDE como IntelliJ IDEA o Eclipse configurados.

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;

public class ConversorMonedas {

    // URL base de la API de Exchange Rate
    private static final String API_URL = "https://open.er-api.com/v6/latest/";
    private static final String API_KEY = "8fde66f7b593105ba55c5fc4";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("¡Bienvenido al Conversor de Monedas!");

        while (true) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Salir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            if (opcion == 1) {
                System.out.print("Ingrese la moneda base (ej. USD): ");
                String monedaBase = scanner.nextLine().toUpperCase();

                System.out.print("Ingrese la moneda destino (ej. ARS): ");
                String monedaDestino = scanner.nextLine().toUpperCase();

                System.out.print("Ingrese el monto a convertir: ");
                double monto = scanner.nextDouble();

                convertirMoneda(monedaBase, monedaDestino, monto);
            } else if (opcion == 2) {
                System.out.println("Gracias por usar el Conversor de Monedas. ¡Hasta pronto!");
                break;
            } else {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

        scanner.close();
    }

    private static void convertirMoneda(String monedaBase, String monedaDestino, double monto) {
        try {
            // Construir la URL de la API
            String urlStr = API_URL + monedaBase + "?apikey=" + API_KEY;
            URL url = new URL(urlStr);

            // Establecer la conexión HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Leer la respuesta de la API
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Analizar la respuesta JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.getString("result").equals("success")) {
                JSONObject rates = jsonResponse.getJSONObject("rates");
                if (rates.has(monedaDestino)) {
                    double tasaCambio = rates.getDouble(monedaDestino);
                    double montoConvertido = monto * tasaCambio;
                    System.out.printf("\n%.2f %s equivalen a %.2f %s\n", monto, monedaBase, montoConvertido, monedaDestino);
                } else {
                    System.out.println("La moneda destino no está disponible.");
                }
            } else {
                System.out.println("Error al obtener los datos de la API. Verifique la moneda base e intente nuevamente.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}
