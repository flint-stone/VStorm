import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class LocalClientOnly {
	
	//String id_c_servertarget = "/home/lexu/workspace/VStorm/resource/";
	int refresh_interval = 5000;
	static int num_servers = 10;
	int num_clients = 1;
	
	public static void main(String[] args) throws InterruptedException{
		(new Thread(new Client())).start();
		while(true){
			//Thread.sleep(refresh_interval);
			Thread.sleep(1000);
			for(int i=1;i<=num_servers;i++){		
				(new Thread(new Server(i))).start();
			}
	        
		}	
		
	}

}


class Client implements Runnable{
	
	String C_STAT_PATH = "/home/lexu/Desktop/storm-starter/resource/";
	int refresh_interval = 5000;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String id = "";
		String ip ="";
		int buffer_size=0;
		int bandwidth =0;
		int framerate=0;
		while(true){
			try {
				Thread.sleep(refresh_interval);
				
				File file = new File(C_STAT_PATH+"c_stat");
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				line = br.readLine();
				String[] parts = line.split(":");
				id = parts[0];
				ip = parts[1];
				//String port = parts[2];
				buffer_size = Integer.valueOf(parts[2]);
				bandwidth = Integer.valueOf(parts[3]);
				framerate = Integer.valueOf(parts[4]);
				br.close();
				//write
				FileWriter llWriter;
				llWriter = new FileWriter(file, false); // false to overwrite.
				Random rand = new Random();
				int rand1 =rand.nextInt(2001)-1000;
				int rand2 =rand.nextInt(2001)-1000;
				int rand3 =30-rand.nextInt(21);
				line = id+":"+ip+":"+String.valueOf(buffer_size+ rand1)+":"+String.valueOf(bandwidth+rand2)+":"+String.valueOf(rand3);
                llWriter.write(line);
                llWriter.close();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

class Server implements Runnable{
	
	String id_s_resource = "/home/lexu/Desktop/storm-starter/resource/";
	int id;

	public Server(int i) {
		// TODO Auto-generated constructor stub
		id = i;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			File file = new File(id_s_resource+id+"_s_resource");
			
			//write
			FileWriter llWriter;
			llWriter = new FileWriter(file, false); // false to overwrite.
			Random rand = new Random();
			int rand1 =rand.nextInt(2001)-1000;
            llWriter.write(String.valueOf(5000+rand1));
            llWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}