package Form;

import Lib.Files.FileManager;
import Lib.Graphics.Ground;
import Lib.Entities.Writ;
import Mapper.Data.D;
import Mapper.Logic.WritUpdater;
import Mapper.Mapper;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class SaveWindow extends javax.swing.JFrame {

    private static final String endl = System.getProperty("line.separator");
    private static final String slash = System.getProperty("file.separator");
    private static String previousDir;
    
    DefaultListModel list;
    
    public SaveWindow() {
        initComponents();
        if (FileManager.fileExists("Saves")) {
            list = new DefaultListModel();
            File[] directories = FileManager.getFile("Saves/").listFiles();
            FileWrapper[] wrap = new FileWrapper[directories.length];
            for (int i=0; i<directories.length; i++) {
                wrap[i] = new FileWrapper(directories[i]);
                list.addElement(wrap[i]);
            }
            jList1.setModel(list);
        }
        else {
            FileManager.createDirectory("Saves/");
        }
    }
    
    private static String saveMaker() {
        StringBuilder maps = new StringBuilder();
        maps.append("1.1|").append(endl);
        maps.append(Mapper.width).append(" ").append(Mapper.height).append(endl);
        
        for (int x=0; x<=Mapper.width; x++) {
            for (int y=0; y<=Mapper.height; y++) {
                if (Mapper.heightmap[x][y]!=0f) {
                    saveHeight(maps, x, y);
                }
            }
        }
        
        for (int x=0; x<Mapper.width; x++) {
            for (int y=0; y<Mapper.height; y++) {
                if (!Mapper.ground[x][y].shortName.equals(D.grounds.get(0).shortName)) {
                    saveGround(maps, x, y);
                }
            }
        }
        
        for (int x=0; x<Mapper.width; x++) {
            for (int y=0; y<Mapper.height; y++) {
                if (!Mapper.caveGround[x][y].shortName.equals(D.caveGrounds.get(0).shortName)) {
                    saveCave(maps, x, y);
                }
            }
        }
        
        for (int y=0; y<15; y++) {
            for (int x=0; x<Mapper.width*4; x++) {
                for (int z=0; z<Mapper.height*4; z++) {
                    if (Mapper.objects[x][y][z]!=null) {
                        saveObject(maps, x, y, z);
                    }
                }
            }
        }
        
        for (int y=0; y<15; y++) {
            for (int x=0; x<Mapper.width; x++) {
                for (int z=0; z<Mapper.height; z++) {
                    if (Mapper.tiles[x][y][z]!=null) {
                        saveTile(maps, x, y, z);
                    }
                }
            }
        }
        
        for (int y=0; y<15; y++) {
            for (int x=0; x<Mapper.width; x++) {
                for (int z=0; z<Mapper.height; z++) {
                    if (Mapper.bordersx[x][y][z]!=null) {
                        saveBorderX(maps, x, y, z);
                    }
                }
            }
        }
        
        for (int y=0; y<15; y++) {
            for (int x=0; x<Mapper.width; x++) {
                for (int z=0; z<Mapper.height; z++) {
                    if (Mapper.bordersy[x][y][z]!=null) {
                        saveBorderY(maps, x, y, z);
                    }
                }
            }
        }
        
        for (int x=0; x<Mapper.width; x++) {
            for (int y=0; y<Mapper.height; y++) {
                if (Mapper.labels[x][y]!=null) {
                    saveLabel(maps, x, y);
                }
                if (Mapper.caveLabels[x][y]!=null) {
                    saveCaveLabel(maps, x, y);
                }
            }
        }
        
        if (!WritUpdater.model.isEmpty()) {
            for (int i=0; i<WritUpdater.model.size(); i++) {
                Writ w = (Writ)WritUpdater.model.get(i);
                saveWrit(maps, w);
            }
        }
        return maps.toString();
    }
    
    public static void saveHeight(StringBuilder builder, int x, int y) {
        builder.append("H ").append(x).append(" ").append(y).append(" ").append((int)Mapper.heightmap[x][y]).append(endl);
    }
    
    public static void saveGround(StringBuilder builder, int x, int y) {
        builder.append("G ").append(x).append(" ").append(y).append(" ").append(Mapper.ground[x][y].shortName).append(endl);
    }
    
    public static void saveCave(StringBuilder builder, int x, int y) {
        builder.append("C ").append(x).append(" ").append(y).append(" ").append(Mapper.caveGround[x][y].shortName).append(endl);
    }
    
    /**
     * 1. Type
     * 2. x coord
     * 3. y coord
     * 4. y coord
     * 5. Shortname
     * 6. Red
     * 7. Green
     * 8. Blue
     * 9. Rotation
     */
    public static void saveObject(StringBuilder builder, int x, int y, int z) {
        builder.append("O ").append(x).append(" ").append(y).append(" ").append(z).append(" ").append(Mapper.objects[x][y][z].shortName).append(" ").append(Mapper.objects[x][y][z].rPaint).append(" ").append(Mapper.objects[x][y][z].gPaint).append(" ").append(Mapper.objects[x][y][z].bPaint).append(" ").append(Mapper.objects[x][y][z].rotation).append(endl);
    }
    
    public static void saveTile(StringBuilder builder, int x, int y, int z) {
        builder.append("T ").append(x).append(" ").append(y).append(" ").append(z).append(" ").append(Mapper.tiles[x][y][z].shortName).append(endl);
    }
    
    public static void saveBorderX(StringBuilder builder, int x, int y, int z) {
        builder.append("BX ").append(x).append(" ").append(y).append(" ").append(z).append(" ").append(Mapper.bordersx[x][y][z].shortName).append(" ").append(Mapper.bordersx[x][y][z].rPaint).append(" ").append(Mapper.bordersx[x][y][z].gPaint).append(" ").append(Mapper.bordersx[x][y][z].bPaint).append(endl);
    }
    
    public static void saveBorderY(StringBuilder builder, int x, int y, int z) {
        builder.append("BY ").append(x).append(" ").append(y).append(" ").append(z).append(" ").append(Mapper.bordersy[x][y][z].shortName).append(" ").append(Mapper.bordersy[x][y][z].rPaint).append(" ").append(Mapper.bordersy[x][y][z].gPaint).append(" ").append(Mapper.bordersy[x][y][z].bPaint).append(endl);
    }
    
    /**
     * 1. Type
     * 2. x coord
     * 3. y coord
     * 4. Text
     * 5. Font
     * 6. Size
     * 7. Red
     * 8. Green
     * 9. Blue
     * 10. Alpha
     * 11. Cave
     */
    public static void saveLabel(StringBuilder builder, int x, int y) {
        builder.append("L ").append(x).append(" ").append(y).append(" ").append(Mapper.labels[x][y].text.replace(" ", "_").replace("\n", "\\n")).append(" ").append(Mapper.labels[x][y].font.getName().replace(" ", "_")).append(" ").append(Mapper.labels[x][y].font.getSize()).append(" ").append(Mapper.labels[x][y].color.getRed()).append(" ").append(Mapper.labels[x][y].color.getGreen()).append(" ").append(Mapper.labels[x][y].color.getBlue()).append(" ").append(Mapper.labels[x][y].color.getAlpha()).append(" ").append(false).append(endl);
    }
    
    public static void saveCaveLabel(StringBuilder builder, int x, int y) {
        builder.append("L ").append(x).append(" ").append(y).append(" ").append(Mapper.caveLabels[x][y].text.replace(" ", "_").replace("\n", "\\n")).append(" ").append(Mapper.caveLabels[x][y].font.getName().replace(" ", "_")).append(" ").append(Mapper.caveLabels[x][y].font.getSize()).append(" ").append(Mapper.caveLabels[x][y].color.getRed()).append(" ").append(Mapper.caveLabels[x][y].color.getGreen()).append(" ").append(Mapper.caveLabels[x][y].color.getBlue()).append(" ").append(Mapper.caveLabels[x][y].color.getAlpha()).append(" ").append(true).append(endl);
    }
    
    public static void saveWrit(StringBuilder builder, Writ writ) {
        builder.append("W ").append(writ.name.replace(" ", "_")).append(" ").append(writ.tiles.size());
        for (Ground g : writ.tiles) {
            builder.append(" ").append(g.x).append(" ").append(g.y);
        }
        builder.append(endl);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        pasteExpiration = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Save map");
        setMinimumSize(new java.awt.Dimension(500, 400));

        jButton1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton1.setText("Save to file");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton2.setText("Save to pastebin.com");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Saved map");

        jButton3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton3.setText("Copy code");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Quick save");

        jList1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jList1.setFixedCellHeight(20);
        jScrollPane1.setViewportView(jList1);

        jButton4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextField1.setText("Save name");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton5.setText("Overwrite selected");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton6.setText("Delete selected");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        pasteExpiration.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pasteExpiration.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10 minutes", "1 hour", "1 day", "1 week", "2 weeks", "1 month", "Never delete" }));
        pasteExpiration.setSelectedIndex(5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pasteExpiration, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(pasteExpiration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            String data;
            data = URLEncoder.encode("api_dev_key", "UTF-8") + "=" + URLEncoder.encode("24844c99ae9971a2da79a2f7d0da7642", "UTF-8");
            data += "&" + URLEncoder.encode("api_paste_code", "UTF-8") + "=" + URLEncoder.encode(saveMaker(), "UTF-8");
            data += "&" + URLEncoder.encode("api_option", "UTF-8") + "=" + URLEncoder.encode("paste", "UTF-8");
            switch ((String)pasteExpiration.getModel().getSelectedItem()) {
                case "10 minutes":
                    data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" + URLEncoder.encode("10M", "UTF-8");
                    break;
                case "1 hour":
                    data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" + URLEncoder.encode("1H", "UTF-8");
                    break;
                case "1 day":
                    data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" + URLEncoder.encode("1D", "UTF-8");
                    break;
                case "1 week":
                    data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" + URLEncoder.encode("1W", "UTF-8");
                    break;
                case "2 weeks":
                    data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" + URLEncoder.encode("2W", "UTF-8");
                    break;
                case "1 month":
                    data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" + URLEncoder.encode("1M", "UTF-8");
                    break;
                case "Never delete":
                    data += "&" + URLEncoder.encode("api_paste_expire_date", "UTF-8") + "=" + URLEncoder.encode("N", "UTF-8");
                    break;
            }
            URL url = new URL("http://pastebin.com/api/api_post.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                Desktop.getDesktop().browse(new URI(line));
            }
            wr.close();
            rd.close();
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(SaveWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fc = new JFileChooser();
        FileFilter filter = new ExtensionFileFilter(".MAP file", new String[] { "MAP" });
        fc.setFileFilter(filter);
        if (previousDir!=null) {
            fc.setCurrentDirectory(new File(previousDir));
        }
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            saveToFile(file);
            setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void saveToFile(File file) {
        if (!file.getPath().contains(".MAP")) {
            file = new File(file.getPath()+".MAP");
        }
        try {
            file.createNewFile();
            PrintWriter out = new PrintWriter(new FileOutputStream(file));
            out.print(saveMaker());
            out.close();
            String ph = file.getPath();
            previousDir = ph.substring(0, ph.lastIndexOf(slash)+1);
        } catch (IOException ex) {
            Logger.getLogger(SaveWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        StringSelection select = new StringSelection(saveMaker());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(select, select);
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        saveToFile(FileManager.getFile("Saves"+slash+jTextField1.getText()+".MAP"));
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (!jList1.isSelectionEmpty()) {
            FileWrapper wrap = (FileWrapper)jList1.getSelectedValue();
            list.removeElement(wrap);
            wrap.file.delete();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!jList1.isSelectionEmpty()) {
            saveToFile(((FileWrapper)jList1.getSelectedValue()).file);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        jTextField1.setText("");
    }//GEN-LAST:event_jTextField1MouseClicked

    private class ExtensionFileFilter extends FileFilter {
        String description;
        String extensions[];

        public ExtensionFileFilter(String description, String extension) {
          this(description, new String[] { extension });
        }

        public ExtensionFileFilter(String description, String extensions[]) {
          if (description == null) {
            this.description = extensions[0];
          } else {
            this.description = description;
          }
          this.extensions = (String[]) extensions.clone();
          toLower(this.extensions);
        }

        private void toLower(String array[]) {
          for (int i = 0, n = array.length; i < n; i++) {
            array[i] = array[i].toLowerCase();
          }
        }

        public String getDescription() {
          return description;
        }

        public boolean accept(File file) {
          if (file.isDirectory()) {
            return true;
          } else {
            String path = file.getAbsolutePath().toLowerCase();
            for (int i = 0, n = extensions.length; i < n; i++) {
              String extension = extensions[i];
              if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
                return true;
              }
            }
          }
          return false;
        }
    }
    
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SaveWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox pasteExpiration;
    // End of variables declaration//GEN-END:variables
}
