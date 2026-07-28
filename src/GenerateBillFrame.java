// ================= FILE 1 =================
// GenerateBillFrame.java
// FULL FIXED BUTTON + PREMIUM UI

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class GenerateBillFrame extends JFrame implements ActionListener {

    JTextField cid, ele, water, gas, internet;
    JComboBox<String> month;
    JLabel totalLbl;

    JButton calcBtn, saveBtn, clearBtn, closeBtn;

    double total = 0;

    public GenerateBillFrame() {

        setTitle("Utility Edge - Generate Bill");
        setSize(900,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setBackground(new Color(8,25,60));

        JLabel title = new JLabel("Generate Utility Bill");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));

        top.add(title);

        add(top, BorderLayout.NORTH);

        GradientPanel main = new GradientPanel();
        main.setLayout(new GridBagLayout());

        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(620,540));
        card.setBackground(new Color(255,255,255,230));
        card.setBorder(new EmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cid = field();
        ele = field();
        water = field();
        gas = field();
        internet = field();

        month = new JComboBox<>(new String[]{
            "January","February","March","April",
            "May","June","July","August",
            "September","October","November","December"
        });

        addRow(card, gbc,0,"Customer ID",cid);
        addRow(card, gbc,1,"Billing Month",month);
        addRow(card, gbc,2,"Electricity Units",ele);
        addRow(card, gbc,3,"Water Units",water);
        addRow(card, gbc,4,"Gas Units",gas);
        addRow(card, gbc,5,"Internet Charge",internet);

        gbc.gridx=0; gbc.gridy=6;

        JLabel t = new JLabel("Total Amount");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        card.add(t, gbc);

        gbc.gridx=1;

        totalLbl = new JLabel("₹ 0");
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        totalLbl.setForeground(new Color(0,120,215));
        card.add(totalLbl, gbc);

        calcBtn = btn("Calculate");
        saveBtn = btn("Save Bill");
        clearBtn = btn("Clear");
        closeBtn = btn("Close");

        JPanel bp = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
        bp.setOpaque(false);

        bp.add(calcBtn);
        bp.add(saveBtn);
        bp.add(clearBtn);
        bp.add(closeBtn);

        gbc.gridx=0; gbc.gridy=7; gbc.gridwidth=2;
        card.add(bp, gbc);

        main.add(card);

        add(main, BorderLayout.CENTER);

        calcBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        setVisible(true);
    }

    void addRow(JPanel p, GridBagConstraints gbc,int y,String txt,JComponent c){

        gbc.gridx=0; gbc.gridy=y; gbc.gridwidth=1;

        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(l, gbc);

        gbc.gridx=1;
        c.setPreferredSize(new Dimension(250,38));
        p.add(c, gbc);
    }

    JTextField field(){
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return t;
    }

    JButton btn(String txt){

        JButton b = new JButton(txt);
        b.setPreferredSize(new Dimension(130,42));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(8,25,60));
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(Color.WHITE,1,true));

        return b;
    }

    void calculate(){

        try{

            total =
                    Integer.parseInt(ele.getText())*8 +
                    Integer.parseInt(water.getText())*4 +
                    Integer.parseInt(gas.getText())*6 +
                    Double.parseDouble(internet.getText());

            totalLbl.setText("₹ "+total);

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Enter valid values");
        }
    }

    void saveBill(){

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
            "insert into bills(customer_id,month,electricity_units,water_units,gas_units,internet_charge,total_amount,status,due_date) values(?,?,?,?,?,?,?,?,?)");

            ps.setInt(1,Integer.parseInt(cid.getText()));
            ps.setString(2,month.getSelectedItem().toString());
            ps.setInt(3,Integer.parseInt(ele.getText()));
            ps.setInt(4,Integer.parseInt(water.getText()));
            ps.setInt(5,Integer.parseInt(gas.getText()));
            ps.setDouble(6,Double.parseDouble(internet.getText()));
            ps.setDouble(7,total);
            ps.setString(8,"Pending");
            ps.setDate(9,java.sql.Date.valueOf(LocalDate.now().plusDays(15)));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Bill Saved");

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e);
        }
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource()==calcBtn) calculate();
        if(e.getSource()==saveBtn) saveBill();
        if(e.getSource()==clearBtn){
            cid.setText(""); ele.setText("");
            water.setText(""); gas.setText("");
            internet.setText(""); totalLbl.setText("₹ 0");
        }
        if(e.getSource()==closeBtn) dispose();
    }

    class GradientPanel extends JPanel{
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
}