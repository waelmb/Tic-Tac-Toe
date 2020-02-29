import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{


	Socket socketClient;
	GameInfo info;
	ObjectOutputStream out;
	ObjectInputStream in;
	String ipAddress;
	int port;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call){
		callback = call;
	}

	public void run() {

		try {
			socketClient= new Socket(ipAddress, port);
			System.out.println("ip:" + ipAddress + " port:" + port);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			callback.accept("Failed to connect to server");
			System.out.println("Failed to connect to server");
			System.out.println("ip:" + ipAddress + " port:" + port);
		}

		while(true) {
			try {
				info = (GameInfo) in.readObject();
				System.out.println("** Client read");
				info.printElems();
				callback.accept("received game information from server");
			}
			catch(Exception e) {
				callback.accept("couldn't receive game info from server");
			}
			if(socketClient.isClosed()) {
				System.out.println("Exiting..");
				break;
			}
		}

	}

	public void send() {
		try {
			System.out.println("** Client (send)" );
			info.printElems();
			out.reset();
			out.writeObject(info);
			out.flush();
			callback.accept("sent game information to server");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't send information to server");
		}
	}


}
