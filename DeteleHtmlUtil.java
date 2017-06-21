package wy.com.write.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wy.com.tool.MD5Util;

public class DeteleHtmlUtil {
	
	
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
	private static final String regEx_w = "<w[^>]*?>[\\s\\S]*?<\\/w[^>]*?>";// 定义所有w标签
	
	/**
	 * 传入文本 返回取出html 空格 标点 后的 md516位 <key，字符串>
	 * @param content
	 * @return
	 * 2017年5月16日
	 */
	public static HashMap<String, String> similarity(String content){
		if(content ==null){
			return null;
		}else{
			HashMap<String,String> map = new HashMap<String,String>();
			//去除html标签
			content = delHTMLTag(content);
			
			//句号分割
			ArrayList<String> lists = splitSentence(content);
			int size = lists.size();
			
			for(int i =0;i<size;i++){
				String cc = lists.get(i);
				//去除标点符号
				String cc2= delPunctuation(cc);
				cc2 = cc2.replaceAll("\\s*","");  
				//md5  16位之后 作为key 加入到map中
				String md516 = MD5Util.MD516(cc2);
				map.put(md516, cc2.trim());
			}
			return map;
		}
	}
	
	/**
	 * 去除标点符号
	 * @param cc
	 * @return
	 * 2017年5月16日
	 */
	private static String delPunctuation(String cc) {
		cc = cc.replaceAll("[\\pP‘’“”]","");  
		return cc.trim();
	}

	/**
	 * 句号分割
	 * @param content
	 * @return
	 * 2017年5月16日
	 */
	private static ArrayList<String> splitSentence(String content) {
		ArrayList<String> lists = new ArrayList<String>();
		if(content.contains(".")||content.contains("。") ){
			content = content.replaceAll("。", ".");
			String[] split = content.split("\\.");
			for (String string : split) {
				lists.add(string);
			}
		}else if(content.contains("!")  ){
			content = content.replaceAll("!", ".");
			String[] split = content.split("\\.");
			for (String string : split) {
				lists.add(string);
			}
		}else if( content.contains("！") ){
			content = content.replaceAll("！", ".");
			String[] split = content.split("\\.");
			for (String string : split) {
				lists.add(string);
			}
			
		}else{
			lists.add(content);
		}
		
		return lists;
	}

	/**
     * @param htmlStr
     * @return 删除Html标签
     * @author LongJin
     */
    private static  String delHTMLTag(String htmlStr) {
    	
    	//删除&nbsp之类的内容
    /*	htmlStr = htmlStr.replaceAll("&nbsp;", "");
    	htmlStr=htmlStr.replaceAll("&ldquo;", "");
    	htmlStr=htmlStr.replaceAll("&rdquo;", "");
    	htmlStr=htmlStr.replaceAll("&hellip;", "");*/
    	String reqAnd="\\&[a-z]{1,10};";
        Pattern a_w = Pattern.compile(reqAnd, Pattern.CASE_INSENSITIVE);
        Matcher b_w = a_w.matcher(htmlStr);
        htmlStr = b_w.replaceAll(""); //删除&nbsp之类的内容
    	
    	
        Pattern p_w = Pattern.compile(regEx_w, Pattern.CASE_INSENSITIVE);
        Matcher m_w = p_w.matcher(htmlStr);
        htmlStr = m_w.replaceAll(""); // 过滤script标签
 
 
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签
 
 
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签
 
 
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
 
 
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
        Matcher m = p.matcher(htmlStr);  
        htmlStr = m.replaceAll("");
        
        return htmlStr.trim(); // 返回文本字符串
    }

    
}
