import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class CalculatorUI extends JFrame {
//Test: -(-9+3)*10+2 62   (8+3)*0.5  5.5
	private JPanel contentPane;
	private JTextField textField;
	private JLabel label_1;  // 必须在construtor外声明。不然在mathActionPerformed中就会报错。

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalculatorUI frame = new CalculatorUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CalculatorUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 377, 220);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("在下方文本框内输入四则运算表达式，单击“计算”返回结果");
		label.setBounds(18, 26, 343, 16);
		
		textField = new JTextField();
		textField.setBounds(18, 48, 343, 69);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("计算");
		btnNewButton.setBounds(18, 123, 75, 40);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mathActionPerformed(e);
			}
		});
		
		label_1 = new JLabel("计算结果是：");
		label_1.setBounds(105, 134, 78, 16);		
		
		JLabel label_2 = new JLabel(" ");
		label_2.setBounds(189, 148, 180, -25);
		contentPane.setLayout(null);
		contentPane.add(btnNewButton);
		contentPane.add(label_1);
		contentPane.add(label_2);
		contentPane.add(textField);
		contentPane.add(label);
		
	}

	protected void mathActionPerformed(ActionEvent e) {
		if (isExpression(this.textField.getText())) {
		    Calculator me = new Calculator(this.textField.getText());
		    label_1.setText(me.caculate());
		} else {
			label_1.setText("非法表达式");
		}
	}

	// 检验是否为合法表达式
	public static boolean isExpression(String str){
		Pattern pattern_s = Pattern.compile("[0-9\\.\\ \\+\\-\\*\\/\\(\\)]*");
		return pattern_s.matcher(str).matches();
		}
}
