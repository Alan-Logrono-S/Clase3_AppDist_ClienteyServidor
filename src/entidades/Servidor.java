package entidades;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Servidor {

    private Modelo modelo = new Modelo();

    public void operar(int puerto) throws Exception {

        DatagramSocket socket = new DatagramSocket(puerto);
        System.out.println("=== SERVIDOR CALCULADORA INICIADO ===");
        System.out.println("Escuchando en el puerto: " + puerto);
        System.out.println("Esperando peticiones de la interfaz gráfica...");

        while (true) {

            byte[] bufferE = new byte[1024];
            DatagramPacket entrada = new DatagramPacket(bufferE, bufferE.length);
            socket.receive(entrada);


            String recibido = new String(entrada.getData(), 0, entrada.getLength()).trim();
            System.out.println("Datos recibidos del Cliente: [" + recibido + "]");

            try {

                String[] partes = recibido.split(",");
                if (partes.length != 3) throw new Exception("Formato inválido");

                double n1 = Double.parseDouble(partes[0]);
                double n2 = Double.parseDouble(partes[1]);
                String op = partes[2];

                double resultado = 0.0;

                switch (op) {
                    case "+": resultado = modelo.sumar(n1, n2); break;
                    case "-": resultado = n1 - n2; break;
                    case "*": resultado = n1 * n2; break;
                    case "/":
                        if (n2 != 0) {
                            resultado = n1 / n2;
                        } else {
                            enviarRespuesta("Error: Div/0", entrada, socket);
                            continue;
                        }
                        break;
                    default:
                        enviarRespuesta("Op. No Soportada", entrada, socket);
                        continue;
                }


                enviarRespuesta(String.valueOf(resultado), entrada, socket);
                System.out.println("Resultado enviado: " + resultado);

            } catch (Exception e) {
                System.err.println("Error procesando petición: " + e.getMessage());
                enviarRespuesta("Error de Formato", entrada, socket);
            }
        }
    }

    private void enviarRespuesta(String mensaje, DatagramPacket peticionOriginal, DatagramSocket socket) throws Exception {
        byte[] bufferS = mensaje.getBytes();
        DatagramPacket salida = new DatagramPacket(
                bufferS,
                bufferS.length,
                peticionOriginal.getAddress(),
                peticionOriginal.getPort()
        );
        socket.send(salida);
    }
}