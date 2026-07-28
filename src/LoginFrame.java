import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn;

    public LoginFrame() {

        setTitle("Utility Edge - Admin Portal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GradientPanel main = new GradientPanel();
        main.setLayout(new GridLayout(1, 2));
        setContentPane(main);

        // ================= LEFT PANEL =================
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(35, 60, 40, 40));

        // Logo
        ImageIcon logo = new ImageIcon("images/logo.png");
        Image img = logo.getImage().getScaledInstance(230, 230, Image.SCALE_SMOOTH);

        JPanel logoHolder = new JPanel(new GridBagLayout());
        logoHolder.setOpaque(false);
        logoHolder.setMaximumSize(new Dimension(260, 260));

        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setOpaque(true);
        logoLabel.setBackground(new Color(255,255,255,70));
        logoLabel.setBorder(new CompoundBorder(
                new LineBorder(new Color(255,255,255,180),3,true),
                new EmptyBorder(10,10,10,10)
        ));

        logoHolder.add(logoLabel);
        logoHolder.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(logoHolder);

        left.add(Box.createVerticalStrut(18));

        JLabel title = new JLabel("UTILITY EDGE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 52));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(title);

        left.add(Box.createVerticalStrut(22));

        // UNIQUE DESCRIPTION
        JLabel desc = new JLabel(
            "<html><center>" +
            "• AI Based Smart Bill Prediction & Alerts<br><br>" +
            "• Real-Time Electricity, Water & Gas Monitoring<br><br>" +
            "• Fraud Detection, Reports & Payment Analytics<br><br>" +
            "• Secure Admin Dashboard with Smart Controls" +
            "</center></html>"
        );

        desc.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        desc.setForeground(new Color(220,235,255));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setHorizontalAlignment(SwingConstants.CENTER);
        desc.setMaximumSize(new Dimension(620, 280));

        left.add(desc);

        left.add(Box.createVerticalStrut(45));

        JPanel cards = new JPanel(new GridLayout(2,2,18,18));
        cards.setOpaque(false);

        cards.add(makeCard("⚡ Electricity"));
        cards.add(makeCard("💧 Water"));
        cards.add(makeCard("🔥 Gas"));
        cards.add(makeCard("📊 Analytics"));

        left.add(cards);

        // ================= RIGHT PANEL =================
        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(470,700));
        card.setBackground(new Color(255,255,255,220));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(255,255,255,180),2,true),
                new EmptyBorder(30,45,30,45)
        ));

        // Small Logo
        Image small = logo.getImage().getScaledInstance(95,95,Image.SCALE_SMOOTH);
        JLabel smallLogo = new JLabel(new ImageIcon(small));
        smallLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(smallLogo);

        card.add(Box.createVerticalStrut(10));

        JLabel loginTitle = new JLabel("Admin Login");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 40));
        loginTitle.setForeground(new Color(12,52,110));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginTitle);

        JLabel sub = new JLabel("Secure Access Portal");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        sub.setForeground(new Color(90,100,140));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(sub);

        card.add(Box.createVerticalStrut(35));

        JLabel user = new JLabel("Username");
        user.setFont(new Font("Segoe UI", Font.BOLD, 18));
        user.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(user);

        card.add(Box.createVerticalStrut(8));

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(380,50));
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        card.add(usernameField);

        card.add(Box.createVerticalStrut(25));

        JLabel pass = new JLabel("Password");
        pass.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pass.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(pass);

        card.add(Box.createVerticalStrut(8));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(380,50));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        card.add(passwordField);

        card.add(Box.createVerticalStrut(18));

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JCheckBox remember = new JCheckBox("Remember me");
        remember.setOpaque(false);

        JLabel forgot = new JLabel("Forgot password?");
        forgot.setForeground(new Color(30,90,220));

        row.add(remember, BorderLayout.WEST);
        row.add(forgot, BorderLayout.EAST);

        card.add(row);

        card.add(Box.createVerticalStrut(35));

        loginBtn = new JButton("LOGIN");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(380,58));
        loginBtn.setBackground(new Color(0,120,215));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(this);
        card.add(loginBtn);

        card.add(Box.createVerticalStrut(30));

        JLabel secure = new JLabel("Login with Secure Key");
        secure.setForeground(new Color(30,90,220));
        secure.setFont(new Font("Segoe UI", Font.BOLD, 18));
        secure.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(secure);

        right.add(card);

        main.add(left);
        main.add(right);

        setVisible(true);
    }

    JPanel makeCard(String txt){
        JPanel p = new JPanel();
        p.setBackground(new Color(255,255,255,130));
        p.setBorder(new LineBorder(new Color(255,255,255,160),1,true));
        p.setLayout(new GridBagLayout());

        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        l.setForeground(new Color(15,55,120));
        p.add(l);

        return p;
    }

    public void actionPerformed(ActionEvent e){
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if(user.equals("admin") && pass.equals("admin123")) {
            dispose();
            new DashboardFrame();
        }
       else
            JOptionPane.showMessageDialog(this,"Invalid Credentials");
    }

    class GradientPanel extends JPanel{
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2=(Graphics2D)g;

            GradientPaint gp=new GradientPaint(
                    0,0,new Color(0,70,140),
                    getWidth(),getHeight(),
                    new Color(0,160,120));

            g2.setPaint(gp);
            g2.fillRect(0,0,getWidth(),getHeight());

            g2.setColor(new Color(255,255,255,25));
            g2.fillOval(80,100,280,280);
            g2.fillOval(getWidth()-350,120,240,240);
        }
    }

    public static void main(String[] args){
        new LoginFrame();
    }
}