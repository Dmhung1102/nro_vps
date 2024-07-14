package nro.server;

import nro.services.Service;
import nro.utils.Log;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import nro.server.io.Message;

public class MenuQuanLy extends JPanel implements ActionListener {

    private JButton baotri, thaydoiexp, thaydoisk, chatserver, kickplayer, doitien, xemplayer;

    public MenuQuanLy() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        baotri = createButton("BẢO TRÌ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(baotri, gbc);

        thaydoiexp = createButton("ĐỔI EXP SERVER");
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(thaydoiexp, gbc);

        chatserver = createButton("THÔNG BÁO GAME");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(chatserver, gbc);

        kickplayer = createButton("KICK ALL PLAYER");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(kickplayer, gbc);

        xemplayer = createButton("TỔNG PLAYER ONL");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(xemplayer, gbc);

    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(255, 255, 0));
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == baotri) {
            Maintenance.gI().start(15);
            Log.error("Tiến Hành Bảo Trì !\n");
        } else if (e.getSource() == thaydoiexp) {
            String exp = JOptionPane.showInputDialog(this, "Bảng Exp Server\n"
                    + "Exp Server hiện tại: " + Manager.RATE_EXP_SERVER);
            if (exp != null) {
                Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
                Log.error("Exp hiện tại là: " + exp + "\n");
            }
        } else if (e.getSource() == chatserver) {
            String chat = JOptionPane.showInputDialog(this, "Thông Báo Server\n");
            if (chat != null) {
                Message msg = new Message(93);
                try {
                    msg.writer().writeUTF(chat);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                Service.getInstance().sendMessAllPlayer(msg);
                msg.cleanup();
                Log.error("Thông báo: " + chat + "\n");
            }
        } else if (e.getSource() == kickplayer) {
            new Thread(() -> {
                Client.gI().close();
            }).start();
        } else if (e.getSource() == xemplayer) {
            String exp = JOptionPane.showInputDialog(this, "\n"
                    + "TỔNG PLAYER: " + Client.gI().getPlayers().size()
                    + "TỔNG THREAD: " + Thread.activeCount());

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Ngọc rồng New");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Panel panel = new Panel();
                frame.getContentPane().add(panel);
                frame.setSize(400, 200);
                frame.setVisible(true);
            }
        });
    }
}
