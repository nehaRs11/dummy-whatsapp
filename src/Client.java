import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener {

    JTextField writeMessage;
    static JPanel textArea;
    static Box vertical = Box.createVerticalBox();
    static JFrame frame = new JFrame();
    static DataOutputStream dout;

    public Client() {

        frame.setLayout(null);

        JPanel jp = new JPanel();
        jp.setBackground(new Color(7, 94, 84));
        jp.setBounds(0, 0, 400, 70);
        jp.setLayout(null);
        frame.add(jp);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/back.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        jp.add(back);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profilePic = new JLabel(i6);
        profilePic.setBounds(40, 10, 50, 50);
        jp.add(profilePic);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(250, 20, 30, 30);
        jp.add(video);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        phone.setBounds(310, 20, 30, 30);
        jp.add(phone);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel more = new JLabel(i15);
        more.setBounds(360, 20, 10, 25);
        jp.add(more);

        JLabel name = new JLabel("Williams");
        name.setBounds(110, 25, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        jp.add(name);

        textArea = new JPanel();
//        textArea.setBackground(Color.white);
        textArea.setBounds(5, 75, 390, 450);
        frame.add(textArea);

        writeMessage = new JTextField();
        writeMessage.setBackground(new Color(238, 234, 234));
        writeMessage.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        writeMessage.setBounds(5, 535, 310, 50);
        writeMessage.setBorder(BorderFactory.createEmptyBorder());
        frame.add(writeMessage);

        JButton send = new JButton("Send");
        send.setBounds(320, 535, 75, 50);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        frame.add(send);

        frame.setSize(400, 600); // to set the size of frame
        frame.setLocation(800, 100);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    public static JPanel formattedTexts(String out) {
        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.Y_AXIS));
        JLabel text = new JLabel("<html><p style=\"width:150px\">" + out + "</p></html>");
        text.setFont(new Font("Tahoma", Font.PLAIN, 16));
        text.setBackground(Color.lightGray);
        text.setOpaque(true);
        text.setBorder(new EmptyBorder(15, 15, 15, 50));
        output.add(text);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(format.format(cal.getTime()));
        output.add(time);
        return output;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String text = writeMessage.getText();
            JPanel output = formattedTexts(text);
            textArea.setLayout(new BorderLayout());
            JPanel right = new JPanel(new BorderLayout());
            right.add(output, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            textArea.add(vertical, BorderLayout.PAGE_START);
            writeMessage.setText("");
            dout.writeUTF(text);
            frame.repaint();
            frame.invalidate();
            frame.validate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();

        try {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            while (true) {
                textArea.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formattedTexts(msg);
                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                textArea.add(vertical, BorderLayout.PAGE_START);
                frame.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
