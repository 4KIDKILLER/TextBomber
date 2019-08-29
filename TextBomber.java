import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 14508 
 *
 * TextBomber 2.0
 */
public class TextBomber {
	public static void main(String[] args) throws AWTException, IOException {
		System.out.println("倒计时5秒后开始,请将鼠标焦点转至聊天窗口...");
		Timer timer = new Timer();
		timer.schedule(new Task(timer), 1000, 1000);
	}
	/*
	 * 读取文件内容
	 * 
	 * @param string path 文件地址
	 */
	public String readFile(String path) throws IOException {
		// 创建文件
		File file = new File(path);
		String result = "";
		if (!file.exists()) {
			System.out.println("文件不存在!");
		} else {
            //编码格式
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");
            //缓冲
			BufferedReader fileRead = new BufferedReader(isr);
			char[] arr = new char[1024];
			int len = fileRead.read(arr);
			// 重新赋值
			try {
				result = new String(arr, 0, len);
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("文件是空的！");
			}
			fileRead.close();
		}
		TextBomber tb = new TextBomber();
		//剔除回车
		String resultData = tb.replaceBlank(result);
		return resultData;
	}
	/**
	 * 剔除回车符和换行符
	 * @param str 需要剔除的内容
	 * @return
	 */
	public String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
/**
 * @author 14508
 * 倒计时后开始执行
 */
class Task extends TimerTask {
    private Timer timer;

    public Task(Timer timer) {
        this.timer = timer;
    }
    //倒计时时间
	int timeout = 6;

	@Override
	public void run() {
		--timeout;
		System.out.println("—————— " + timeout);
		if(timeout==1) {
            //倒计时结束开始执行
			System.out.println("程序正在执行...");
			// 读取内容
			TextBomber read = new TextBomber();
			String sentence = null;
			try {
				String contentPath = System.getProperty("user.dir")+"\\content.txt";
				// String contentPath = "C:\\Users\\14508\\Desktop\\TextBomber\\content.txt";
				sentence = read.readFile(contentPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Robot robot =null;// 创建机器人对象
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			robot.delay(3000);// 延迟三秒，主要是为了预留出打开窗口的时间，括号内的单位为毫秒
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			String[] authors = sentence.split("[,]");// 字符串根据,分割
            //执行次数
            int numberOf = 0;
			try {
				numberOf = (int) Math.abs(Integer.parseInt(authors[0]));//执行次数
                if(numberOf == 0){
                    System.out.println("执行次数参数错误-10001");
                    System.out.println("已完成");
                    this.timer.cancel();
                }
			} catch (NumberFormatException e) {
				System.out.println("执行次数参数错误-10002");
			}
			for (int j = 0; j < numberOf; j++) {// 循环次数
				for (int i = 1; i < authors.length; i++) {
					String sentencet = authors[i];
					Transferable tText = new StringSelection(sentencet);
					clip.setContents(tText, null);
					// 以下两行按下了ctrl+v，完成粘贴功能
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_V);
					// 释放ctrl按键，像ctrl，退格键，删除键这样的功能性按键，在按下后一定要释放，不然会出问题。crtl如果按住没有释放，在按其他字母按键是，敲出来的回事ctrl的快捷键。
					robot.keyRelease(KeyEvent.VK_CONTROL);
					// 延迟一秒再发送，不然会一次性全发布出去，因为电脑的处理速度很快，每次粘贴发送的速度几乎是一瞬间，所以给人的感觉就是一次性发送了全部。这个时间可以自己改，想几秒发送一条都可以
					robot.delay(500);
					// 回车
					robot.keyPress(KeyEvent.VK_ENTER);
				}
			}
			System.out.println("执行完毕,共计执行："+numberOf+"次");
			this.timer.cancel();
		}
	}
}