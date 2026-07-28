import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DashboardFinal extends JFrame implements ActionListener {

    JButton addBtn, viewBtn, genBtn, billBtn, reportBtn, settingsBtn, logoutBtn;
    JLabel custLbl,billLbl,revLbl,pendLbl,clockLbl,statusLbl,footerLbl;

    Timer timer;

    int customers=0,bills=0,pending=0;
    double revenue=0;

    public DashboardFinal(){

        setTitle("Utility Edge - Final System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel side = new JPanel(new GridBagLayout());
        side.setPreferredSize(new Dimension(245,getHeight()));
        side.setBackground(new Color(7,18,48));
        side.setBorder(new EmptyBorder(20,15,20,15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.insets=new Insets(8,0,8,0);
        gbc.weightx=1;

        JLabel brand = new JLabel("UTILITY EDGE");
        brand.setForeground(Color.WHITE);
        brand.setHorizontalAlignment(SwingConstants.CENTER);
        brand.setFont(new Font("Segoe UI",Font.BOLD,24));

        gbc.gridy=0;
        side.add(brand,gbc);

        addBtn=oval("Add Customer");
        viewBtn=oval("Customers");
        genBtn=oval("Generate Bill");
        billBtn=oval("Bills");
        reportBtn=oval("Reports");
        settingsBtn=oval("Settings");
        logoutBtn=oval("Logout");

        gbc.gridy=1; side.add(addBtn,gbc);
        gbc.gridy=2; side.add(viewBtn,gbc);
        gbc.gridy=3; side.add(genBtn,gbc);
        gbc.gridy=4; side.add(billBtn,gbc);
        gbc.gridy=5; side.add(reportBtn,gbc);
        gbc.gridy=6; side.add(settingsBtn,gbc);

        gbc.gridy=7; gbc.weighty=1;
        side.add(new JLabel(""),gbc);

        gbc.gridy=8; gbc.weighty=0;
        side.add(logoutBtn,gbc);

        add(side,BorderLayout.WEST);

        // ================= MAIN =================
        MainPanel main = new MainPanel();
        main.setLayout(new BorderLayout());

        // TOP
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(18,25,10,25));

        JLabel title = new JLabel("UTILITY EDGE DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,34));

        JPanel right = new JPanel(new GridLayout(2,1));
        right.setOpaque(false);

        clockLbl = smallWhite();
        statusLbl = smallYellow("Welcome, Admin");

        right.add(clockLbl);
        right.add(statusLbl);

        top.add(title,BorderLayout.WEST);
        top.add(right,BorderLayout.EAST);

        main.add(top,BorderLayout.NORTH);

        // BODY
        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10,25,15,25));

        JPanel cards = new JPanel(new GridLayout(1,4,18,18));
        cards.setOpaque(false);

        custLbl=metric();
        billLbl=metric();
        revLbl=metric();
        pendLbl=metric();

        cards.add(card("Customers",custLbl));
        cards.add(card("Bills",billLbl));
        cards.add(card("Revenue",revLbl));
        cards.add(card("Pending",pendLbl));

        body.add(cards,BorderLayout.NORTH);

        JPanel lower = new JPanel(new GridLayout(1,3,18,18));
        lower.setOpaque(false);
        lower.setBorder(new EmptyBorder(18,0,0,0));

        lower.add(new RevenuePanel());
        lower.add(new UsagePanel());
        lower.add(new InsightPanel());

        body.add(lower,BorderLayout.CENTER);

        main.add(body,BorderLayout.CENTER);

        // FOOTER
        footerLbl = new JLabel("Connected to Database | Version 1.0");
        footerLbl.setForeground(Color.WHITE);
        footerLbl.setBorder(new EmptyBorder(8,20,8,20));

        main.add(footerLbl,BorderLayout.SOUTH);

        add(main,BorderLayout.CENTER);

        // TIMER
        timer = new Timer(3000,e->{
            loadData();
            updateClock();
            repaint();
        });

        timer.start();

        loadData();
        updateClock();

        setVisible(true);
    }

    // ================= BUTTON =================
    JButton oval(String txt){

        JButton b = new JButton(txt){

            protected void paintComponent(Graphics g){

                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp;

                if(getModel().isRollover())
                    gp=new GradientPaint(
                            0,0,new Color(0,170,255),
                            getWidth(),0,new Color(0,255,170));
                else
                    gp=new GradientPaint(
                            0,0,new Color(255,255,255,30),
                            getWidth(),0,new Color(255,255,255,10));

                g2.setPaint(gp);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),45,45);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setPreferredSize(new Dimension(185,42));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setBorder(new LineBorder(new Color(255,255,255,70),1,true));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.addActionListener(this);

        return b;
    }

    JLabel metric(){
        JLabel l=new JLabel("0",SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI",Font.BOLD,30));
        l.setForeground(new Color(0,120,215));
        return l;
    }

    JLabel smallWhite(){
        JLabel l=new JLabel("",SwingConstants.RIGHT);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI",Font.BOLD,14));
        return l;
    }

    JLabel smallYellow(String t){
        JLabel l=new JLabel(t,SwingConstants.RIGHT);
        l.setForeground(new Color(255,255,180));
        l.setFont(new Font("Segoe UI",Font.BOLD,14));
        return l;
    }

    JPanel card(String t,JLabel v){

        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(new Color(255,255,255,225));
        p.setBorder(new CompoundBorder(
                new MatteBorder(0,0,6,6,new Color(0,0,0,28)),
                new EmptyBorder(15,15,15,15)));

        JLabel title=new JLabel(t,SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI",Font.BOLD,18));

        p.add(title,BorderLayout.NORTH);
        p.add(v,BorderLayout.CENTER);

        return p;
    }

    // ================= DATABASE =================
    void loadData(){

        try{

            Connection con=DBConnection.getConnection();

            ResultSet r1=con.createStatement()
                    .executeQuery("select count(*) from customers");
            if(r1.next()) customers=r1.getInt(1);

            ResultSet r2=con.createStatement()
                    .executeQuery("select count(*) from bills");
            if(r2.next()) bills=r2.getInt(1);

            ResultSet r3=con.createStatement()
                    .executeQuery("select ifnull(sum(total_amount),0) from bills");
            if(r3.next()) revenue=r3.getDouble(1);

            ResultSet r4=con.createStatement()
                    .executeQuery("select count(*) from bills where status='Pending'");
            if(r4.next()) pending=r4.getInt(1);

        }catch(Exception e){}

        custLbl.setText(""+customers);
        billLbl.setText(""+bills);
        revLbl.setText("₹ "+String.format("%.0f",revenue));
        pendLbl.setText(""+pending);

        footerLbl.setText("Connected to Database | Customers: "+customers+" | Bills: "+bills+" | Version 1.0");
    }

    void updateClock(){

        java.text.SimpleDateFormat sdf=
                new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

        clockLbl.setText(sdf.format(new java.util.Date()));
    }

    // ================= EASY BAR CHART =================
    class RevenuePanel extends JPanel{

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2=(Graphics2D)g;

            g2.setColor(new Color(255,255,255,220));
            g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);

            g2.setFont(new Font("Segoe UI",Font.BOLD,18));
            g2.drawString("Monthly Revenue",15,22);

            int[] val={10,20,30,25,40};
            String[] mon={"Jan","Feb","Mar","Apr","May"};

            int base=getHeight()-50;
            int gap=(getWidth()-80)/5;

            for(int i=0;i<5;i++){

                int h=val[i]*10;
                int x=40+i*gap;

                g2.setColor(new Color(0,120,215));
                g2.fillRoundRect(x,base-h,35,h,10,10);

                g2.setColor(Color.BLACK);
                g2.drawString("₹"+val[i]+"k",x-5,base-h-5);
                g2.drawString(mon[i],x,base+18);
            }
        }
    }

    // ================= EASY DONUT =================
    class UsagePanel extends JPanel{

        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2=(Graphics2D)g;

            g2.setColor(new Color(255,255,255,220));
            g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);

            g2.drawString("Utility Split",15,22);

            int s=180,x=40,y=70;

            g2.setColor(new Color(0,120,215));
            g2.fillArc(x,y,s,s,0,150);

            g2.setColor(new Color(0,180,130));
            g2.fillArc(x,y,s,s,150,120);

            g2.setColor(new Color(255,160,0));
            g2.fillArc(x,y,s,s,270,90);

            g2.setColor(Color.WHITE);
            g2.fillOval(x+50,y+50,80,80);

            g2.setColor(Color.BLACK);
            g2.drawString("40%",x+73,y+95);

            g2.drawString("Blue = Electricity",250,110);
            g2.drawString("Green = Water",250,140);
            g2.drawString("Orange = Gas",250,170);
        }
    }

    // ================= INSIGHTS =================
    class InsightPanel extends JPanel{

        public InsightPanel(){

            setBackground(new Color(255,255,255,220));
            setBorder(new EmptyBorder(20,20,20,20));
            setLayout(new GridLayout(8,1,10,10));

            add(new JLabel("Quick Insights"));
            add(new JLabel("• Today's Revenue : ₹12,450"));
            add(new JLabel("• Pending Bills : 3"));
            add(new JLabel("• Avg Payment : 7 Days"));
            add(new JLabel("• Peak Usage : Evening"));
            add(new JLabel("• Growth Rate : +18%"));
            add(new JLabel("• Active Users : 128"));
            add(new JLabel("• System Healthy"));
        }
    }

    // ================= ACTION =================
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==addBtn) new AddCustomerFrame();
        if(e.getSource()==viewBtn) new ViewCustomersFrame();
        if(e.getSource()==genBtn) new GenerateBillFrame();
        if(e.getSource()==billBtn) new ViewBillsFrame();
        if(e.getSource()==reportBtn) new ReportsFrame();
        if(e.getSource()==settingsBtn) new SettingsFrame();

        if(e.getSource()==logoutBtn){
            dispose();
            new LoginFrame();
        }
    }

    // ================= BG =================
    class MainPanel extends JPanel{

        protected void paintComponent(Graphics g){

            Graphics2D g2=(Graphics2D)g;

            GradientPaint gp=new GradientPaint(
                    0,0,new Color(0,95,180),
                    getWidth(),getHeight(),
                    new Color(0,180,130));

            g2.setPaint(gp);
            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }

    public static void main(String[] args){
        new DashboardFinal();
    }
}