import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProduct extends JFrame {

    private ProductRecords parentScreen;

    public AddProduct(ProductRecords parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Product Details");
        setSize(300, 400);
        setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        JButton addControllerButton = new JButton("Add Controller");
        addControllerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addController();
            }
        });

        JButton AddLocomotiveButton = new JButton("Add Locomotive");
        AddLocomotiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLocomotive();
            }
        });

        JButton addRollingStockButton = new JButton("Add Rolling Stock");
        addRollingStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRollingStock();
            }
        });

        JButton AddTrackButton = new JButton("Add Track");
        AddTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrack();
            }
        });

        JButton AddTrackPackButton = new JButton("Add Track Pack");
        AddTrackPackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrackPack();
            }
        });

        JButton AddTrainSetButton = new JButton("Add Train Set");
        AddTrainSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrainSet();
            }
        });

        detailsPanel.add(addControllerButton);
        detailsPanel.add(AddLocomotiveButton);
        detailsPanel.add(addRollingStockButton);
        detailsPanel.add(AddTrackButton);
        detailsPanel.add(AddTrackPackButton);
        detailsPanel.add(AddTrainSetButton);
        detailsPanel.add(goBackButton);
        add(detailsPanel);
    }

    private void goBack(){
        System.out.println("Go back to product screen");
        parentScreen.setVisible(true);
        this.dispose();
    }
    private void addController(){
        System.out.println("AddController");
        AddController addController = new AddController(this);
        addController.setVisible(true);
        this.setVisible(false);
    }
    private void addLocomotive(){
        System.out.println("AddLocomotive");
        AddLocomotive addLocomotive = new AddLocomotive(this);
        addLocomotive.setVisible(true);
        this.setVisible(false);
    }
    private void addRollingStock(){
        System.out.println("AddRollingStock");
        AddRollingStock addRollingStock = new AddRollingStock(this);
        addRollingStock.setVisible(true);
        this.setVisible(false);
    }
    private void addTrack(){
        System.out.println("AddTrack");
        AddTrack addTrack = new AddTrack(this);
        addTrack.setVisible(true);
        this.setVisible(false);
    }
    private void addTrackPack(){
        System.out.println("AddTrackPack");
        AddTrackPack addTrackPack = new AddTrackPack(this);
        addTrackPack.setVisible(true);
        this.setVisible(false);
    }
    private void addTrainSet(){
        System.out.println("AddTrainSet");
        AddTrainSet addTrainSet = new AddTrainSet(this);
        addTrainSet.setVisible(true);
        this.setVisible(false);
    }
}
