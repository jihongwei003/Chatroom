package Client.ResponseHandlers;

import java.net.Socket;

import net.sf.json.JSONObject;
import Tools.JsonTrans;

public class SetNameHandler extends ResponseHandler {

	public SetNameHandler(Socket socket) {
		super(socket);
	}

	private static String s_tempName;

	public static String getTempName() {
		return s_tempName;
	}

	@Override
	public void handleResponse() {
		// 解析服务器包头
		JSONObject json1 = (JSONObject) JsonTrans.parseJson(super.responseMsg,
				"res");
		String content = json1.getString("content");// 取出content部分

		s_tempName = content;
	}

	private static String s_realName;
	
	public static void setRealName(String name){
		s_realName = name;
	}
	
	public static String getRealName(){
		return s_realName;
	}
	
}
