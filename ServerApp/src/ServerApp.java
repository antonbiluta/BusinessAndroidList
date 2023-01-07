import Business.CompanyOperator;
import Business.Department;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerApp {

    private ServerSocket serverSocket;
    private static CompanyOperator coperator = new CompanyOperator();
    private static String toJSON;
    private final static String filePath = "info.txt";

    public void start(int port) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            toJSON = readFile(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        coperator = gson.fromJson(toJSON, CompanyOperator.class);

        serverSocket = new ServerSocket(port);
        while (true) {
            new EchoClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            String inputLine = null;
            while (true) {
                try {
                    if ((inputLine = in.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }

                if ("{R}".equals(inputLine)) {
                    out.println(toJSON);
                }

                if (inputLine != null) {
                    if ('d' == inputLine.charAt(0)) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] ids = inputLine.substring(1).split(",");
                        int companyID = Integer.parseInt(ids[0]);
                        int departmentID = Integer.parseInt(ids[1]);
                        coperator.delDepartment(companyID, departmentID);
                        toJSON = gson.toJson(coperator);
                        writeFile(filePath, toJSON);
                        out.println(toJSON);
                    }
                    if ('e' == inputLine.charAt(0)) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        String[] ids = parts[0].split(",");
                        int companyID = Integer.parseInt(ids[0]);
                        int departmentID = Integer.parseInt(ids[1]);
                        Department tempDepartment = gson.fromJson(parts[1], Department.class);
                        coperator.editDepartment(companyID, departmentID, tempDepartment);
                        toJSON = gson.toJson(coperator);
                        writeFile(filePath, toJSON);
                        out.println(toJSON);
                    }
                    if ('u' == inputLine.charAt(0)) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        CompanyOperator tempCo = gson.fromJson(inputLine.substring(1), CompanyOperator.class);
                        coperator.setCompanies(tempCo.getCompanies());
                        toJSON = gson.toJson(coperator);
                        writeFile(filePath, toJSON);
                    }
                    if ('a' == inputLine.charAt(0)) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        Department tempDepartment = gson.fromJson(parts[1], Department.class);
                        coperator.addDepartment(parts[0], tempDepartment);
                        toJSON = gson.toJson(coperator);
                        writeFile(filePath, toJSON);
                        out.println(toJSON);
                    }
                }
            }

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            out.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text) {
        try(FileWriter writer = new FileWriter(path, false)) {
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
