package entidades;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Servidor {
    private Modelo modelo = new Modelo();

    public double sumar(double n1, double n2) {
        return n1 + n2;
    }

    public void operar(int puerto) throws Exception {
        //CREAR EL SOCKET PARA EL DATAGRAMA

        DatagramSocket socket = new DatagramSocket(puerto);
        System.out.println("Servidor conectado en el puerto " + puerto);
        while (true) {
            //RECIBIR PETICION DEL CLIENTE

            byte[] bufferE = new byte[1024];
            DatagramPacket entrada = new DatagramPacket(bufferE, bufferE.length);
            socket.receive(entrada);

            //PROCESAR

            String recibido = new String(entrada.getData(), 0, entrada.getLength());
            //arreglo para serpara los numeritos
            String[] partes = recibido.trim().split(",");

            double n1 = Double.parseDouble(partes[0]);
            double n2 = Double.parseDouble(partes[1]);

            String operacion = partes[2];

            double respuesta = 0;

            if(operacion.equals("+")){respuesta = modelo.sumar(n1, n2);}
            if(operacion.equals("-")){respuesta = modelo.restar(n1, n2);}
            if(operacion.equals("*")){respuesta = modelo.multiplicar(n1, n2);}
            if(operacion.equals("/")){respuesta = modelo.dividir(n1, n2);}

    //DEVOLVER LA SOLICITUD

            byte[] bufferS = String.valueOf(respuesta).getBytes();
            DatagramPacket salida = new DatagramPacket(bufferS, bufferS.length, entrada.getAddress(), entrada.getPort());
            socket.send(salida);
        }
    }
    public static void main(String[] args) throws Exception{
        new Servidor().operar(5000);
    }
}
