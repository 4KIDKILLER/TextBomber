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
		System.out.println("����ʱ5���ʼ,�뽫��꽹��ת�����촰��...");
		Timer timer = new Timer();
		timer.schedule(new Task(timer), 1000, 1000);
	}
	/*
	 * ��ȡ�ļ�����
	 * 
	 * @param string path �ļ���ַ
	 */
	public String readFile(String path) throws IOException {
		// �����ļ�
		File file = new File(path);
		String result = "";
		if (!file.exists()) {
			System.out.println("�ļ�������!");
		} else {
            //�����ʽ
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");
            //����
			BufferedReader fileRead = new BufferedReader(isr);
			char[] arr = new char[1024];
			int len = fileRead.read(arr);
			// ���¸�ֵ
			try {
				result = new String(arr, 0, len);
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("�ļ��ǿյģ�");
			}
			fileRead.close();
		}
		TextBomber tb = new TextBomber();
		//�޳��س�
		String resultData = tb.replaceBlank(result);
		return resultData;
	}
	/**
	 * �޳��س����ͻ��з�
	 * @param str ��Ҫ�޳�������
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
 * ����ʱ��ʼִ��
 */
class Task extends TimerTask {
    private Timer timer;

    public Task(Timer timer) {
        this.timer = timer;
    }
    //����ʱʱ��
	int timeout = 6;

	@Override
	public void run() {
		--timeout;
		System.out.println("������������ " + timeout);
		if(timeout==1) {
            //����ʱ������ʼִ��
			System.out.println("��������ִ��...");
			// ��ȡ����
			TextBomber read = new TextBomber();
			String sentence = null;
			try {
				String contentPath = System.getProperty("user.dir")+"\\content.txt";
				// String contentPath = "C:\\Users\\14508\\Desktop\\TextBomber\\content.txt";
				sentence = read.readFile(contentPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Robot robot =null;// ���������˶���
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			robot.delay(3000);// �ӳ����룬��Ҫ��Ϊ��Ԥ�����򿪴��ڵ�ʱ�䣬�����ڵĵ�λΪ����
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			String[] authors = sentence.split("[,]");// �ַ�������,�ָ�
            //ִ�д���
            int numberOf = 0;
			try {
				numberOf = (int) Math.abs(Integer.parseInt(authors[0]));//ִ�д���
                if(numberOf == 0){
                    System.out.println("ִ�д�����������-10001");
                    System.out.println("�����");
                    this.timer.cancel();
                }
			} catch (NumberFormatException e) {
				System.out.println("ִ�д�����������-10002");
			}
			for (int j = 0; j < numberOf; j++) {// ѭ������
				for (int i = 1; i < authors.length; i++) {
					String sentencet = authors[i];
					Transferable tText = new StringSelection(sentencet);
					clip.setContents(tText, null);
					// �������а�����ctrl+v�����ճ������
					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_V);
					// �ͷ�ctrl��������ctrl���˸����ɾ���������Ĺ����԰������ڰ��º�һ��Ҫ�ͷţ���Ȼ������⡣crtl�����סû���ͷţ��ڰ�������ĸ�����ǣ��ó����Ļ���ctrl�Ŀ�ݼ���
					robot.keyRelease(KeyEvent.VK_CONTROL);
					// �ӳ�һ���ٷ��ͣ���Ȼ��һ����ȫ������ȥ����Ϊ���ԵĴ����ٶȺܿ죬ÿ��ճ�����͵��ٶȼ�����һ˲�䣬���Ը��˵ĸо�����һ���Է�����ȫ�������ʱ������Լ��ģ��뼸�뷢��һ��������
					robot.delay(500);
					// �س�
					robot.keyPress(KeyEvent.VK_ENTER);
				}
			}
			System.out.println("ִ�����,����ִ�У�"+numberOf+"��");
			this.timer.cancel();
		}
	}
}