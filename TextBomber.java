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
		System.out.println("Countdown 5 seconds after the start, please mouse focus to chat window...");
		Timer timer = new Timer();
		timer.schedule(new Task(timer), 1000, 1000);
	}
	public String readFile(String path) throws IOException {
		File file = new File(path);
		String result = "";
		if (!file.exists()) {
			System.out.println("Error:File does not exist - 10001");
		} else {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");
			BufferedReader fileRead = new BufferedReader(isr);
			char[] arr = new char[1024];
			int len = fileRead.read(arr);
			try {
				result = new String(arr, 0, len);
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("Error:The file is empty - 10002");
			}
			fileRead.close();
		}
		TextBomber tb = new TextBomber();
		String resultData = tb.replaceBlank(result);
		return resultData;
	}
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
class Task extends TimerTask {
    private Timer timer;

    public Task(Timer timer) {
        this.timer = timer;
    }
    
	int timeout = 6;

	@Override
	public void run() {
		--timeout;
		System.out.println("--------" + timeout);
		if(timeout==1) {
			System.out.println("The program is executing...");
			TextBomber read = new TextBomber();
			String sentence = null;
			try {
				String contentPath = System.getProperty("user.dir")+"\\content.txt";
				sentence = read.readFile(contentPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Robot robot =null;
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			robot.delay(3000);
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			String[] authors = sentence.split("[,]");
            int numberOf = 0;
			try {
				numberOf = (int) Math.abs(Integer.parseInt(authors[0]));
                if(numberOf == 0){
                    System.out.println("Error:Parameter error - 10003");
                    this.timer.cancel();
                }
			} catch (NumberFormatException e) {
				System.out.println("Error:parameter error - 10004");
			}
			for (int j = 0; j < numberOf; j++) {
				for (int i = 1; i < authors.length; i++) {
					String sentencet = authors[i];
					Transferable tText = new StringSelection(sentencet);
					clip.setContents(tText, null);
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_V);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					robot.delay(500);
					robot.keyPress(KeyEvent.VK_ENTER);
				}
			}
			System.out.println("Completed:A total of "+numberOf+" times");
			this.timer.cancel();
		}
	}
}