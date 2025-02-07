package Client;

import java.net.*;
import java.util.HashMap;
import java.io.*;

import Client.ResCollection.ResponseCollectionManager;
import Client.ResponseHandlers.ResDisplayFriendsHandler;
import Client.ResponseHandlers.ResGroupChatHandler;
import Client.ResponseHandlers.ResLoginHandler;
import Client.ResponseHandlers.ResPrivateChatHandler;
import Client.ResponseHandlers.ResRegistHandler;
import Client.ResponseHandlers.ResponseHandler;
import Client.ResponseHandlers.SetNameHandler;

public class Client {

	public Client() {
	}

	// 主要负责两条线程的依赖注入
	public void runClient() {
		try {
			Socket socket = new Socket("localhost", 8888);

			// 回复的消息集合
			ResponseCollectionManager resCollec = new ResponseCollectionManager();

			// 策略map
			HashMap<String, ResponseHandler> responseMap = new HashMap<String, ResponseHandler>();

			ResponseHandler resRegistHandler = new ResRegistHandler(socket);
			ResponseHandler resLoginHandler = new ResLoginHandler(socket);
			ResponseHandler resDisplayFriendsHandler = new ResDisplayFriendsHandler(socket);
			ResponseHandler resPrivateChatHandler = new ResPrivateChatHandler(socket);
			ResponseHandler resGroupChatHandler = new ResGroupChatHandler(socket);
			ResponseHandler setNameHandler = new SetNameHandler(socket);
			
			responseMap.put("5", resRegistHandler);
			responseMap.put("6", resLoginHandler);
			responseMap.put("1", resDisplayFriendsHandler);
			responseMap.put("2", resPrivateChatHandler);
			responseMap.put("3", resGroupChatHandler);
			responseMap.put("0", setNameHandler);
			
			// 读取来自server消息的线程
			ClientReaderThread readerThread = new ClientReaderThread(socket);
			readerThread.setResponseCollectionManager(resCollec);
			readerThread.setDaemon(true); // 将线程改为守护进程
			readerThread.start();

			// 处理消息
			ResponseManager manager = new ResponseManager(socket);
			manager.setResponseCollectionManager(resCollec);
			manager.setResponseMap(responseMap); // 注入策略map
			manager.runManager();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		Client client = new Client();
		client.runClient();

	}
}
