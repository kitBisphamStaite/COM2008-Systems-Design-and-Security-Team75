import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditControllers extends JFrame {	
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
    Connection connection;
    //SQL
    Statement getAllControllerstmt;
    ResultSet ControllerListResultSet;
    Vector<String> ControllerVector = new Vector<String>();
    Vector<String> productInformationVector = new Vector<String>();
    //SelectedValue from List
	String selectedValue;
	int selectedIndex;
	String selectedValueProductCode;
	JList<String> controllerList = new JList<String>();
	//New variables depending on category
	String selectedValueChipType;
	String selectedValueInformation;
	
	//Variables in every category
	String selectedValueCost;
	String selectedValueName;
	String selectedValueStock;
	String selectedValueGauge;
	String selectedValueScale;
	
    public EditControllers() {
        //GET LIST OF TRACKS
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            getAllControllerstmt = connection.createStatement();
            //Get All Track Products
            ControllerListResultSet = getAllControllerstmt.executeQuery("SELECT * FROM Products INNER JOIN Controller ON Products.product_code = Controller.product_code");
            
            while (ControllerListResultSet.next()) {
                String row = ControllerListResultSet.getString("product_code") + ", " + ControllerListResultSet.getString("chip_type");
                String productRow = ControllerListResultSet.getString("product_name") + ", £" + ControllerListResultSet.getString("retail_price") + ", " + ControllerListResultSet.getString("stock") + ", " + ControllerListResultSet.getString("gauge") + ", " + ControllerListResultSet.getString("scale");
                productInformationVector.add(productRow);
                ControllerVector.add(row);
            }
  
            //Set Up Frame
            setTitle("Edit Controllers");
            setSize(1024, 768);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
                      
            //Create Header Panel Items
            //*Back Button
            JButton backButton = new JButton("Back"); //Take user back to Staff Dashboard Screen
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StaffDashboard staffDashboardScreen = new StaffDashboard();
                    staffDashboardScreen.setLocationRelativeTo(null);
                    staffDashboardScreen.setVisible(true);
                    setVisible(false);
                }
            });
            //Create Header Panel
            JPanel headerPanel = new JPanel(new BorderLayout());
            //*Trains Of Sheffield Header
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Controller Products");
            trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
            //Add Items To Header Panel
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
            
            //Create Panels
            JPanel tracksListPanel = new JPanel(new BorderLayout());
            JPanel addOrDeletePanel = new JPanel(new GridLayout(0,2));
            JPanel selectedProductDetailsPanel = new JPanel(new GridLayout(15,0,0,0));
            //Add Panels To Frame
            add(headerPanel, BorderLayout.NORTH);
            add(tracksListPanel, BorderLayout.CENTER);
            add(addOrDeletePanel, BorderLayout.SOUTH);
            tracksListPanel.add(selectedProductDetailsPanel, BorderLayout.CENTER);
            
            
            
            
            
            
            //Create Track List Panel Items
            JLabel controllerListLabel = new JLabel("Track Product List:");
            JList<String> controllerList = new JList<String>(ControllerVector);
            JList<String> productInformationList = new JList<String>(productInformationVector);
            controllerList.setPreferredSize(new Dimension(200, 200));
            controllerList.setSelectedIndex(0);
            productInformationList.setSelectedIndex(0);
            selectedValueInformation = productInformationList.getSelectedValue();
            selectedIndex = 0;
            selectedValue = controllerList.getSelectedValue();
            JLabel productInfo = new JLabel("Selected Controller Product Info:");
            productInfo.setFont(new Font("Dialog", Font.BOLD, 22));
            selectedProductDetailsPanel.add(productInfo);
            JLabel productName = new JLabel("Product Name: ");
            productName.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productName);
            JLabel productCode = new JLabel("Product Code: ");
            productCode.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productCode);
            JLabel chipType = new JLabel("Product Chip Type: ");
            chipType.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(chipType);
            JLabel productCost = new JLabel("Product Cost: £");
            productCost.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productCost);
            JLabel productStock = new JLabel("Product Stock: ");
            productStock.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productStock);
            JLabel productGauge = new JLabel("Product Gauge: ");
            productGauge.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productGauge);
            JLabel productScale = new JLabel("Product Scale: ");
            productScale.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productScale);
            
            
            
            
            
            
            
            JButton editProductButton = new JButton("Edit Product");
            editProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (selectedValue != null) {
                    	String[] detailToEditChoices = {"Name", "Chip Type", "Cost", "Stock", "Gauge", "Scale"};
        				String detailToEdit = (String) JOptionPane.showInputDialog(null, "Select Detail To Edit", "Edit Existing Controller Product", JOptionPane.QUESTION_MESSAGE, null, detailToEditChoices, detailToEditChoices[0]);
        				try {
        					EditControllers editControllers;
        					String selectedProduct = controllerList.getSelectedValue();
        					selectedValueProductCode = selectedValue.substring(0, selectedValue.indexOf(","));
        					if (detailToEdit != null) {
                				switch(detailToEdit) {
                				case "Name":
                					String newProductName = JOptionPane.showInputDialog(null, "Enter New Name For Product", "Edit Existing Controller Product", JOptionPane.INFORMATION_MESSAGE);
                					if (newProductName == null) {
                						break;
                					}
                					PreparedStatement editProductNamestmt = connection.prepareStatement("UPDATE Products SET product_name = '" + newProductName +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductNamestmt.executeUpdate();
                					dispose();
                					editControllers = new EditControllers();
                					editControllers.setLocationRelativeTo(null);
                					break;
                				case "Chip Type":
                					String[] chipTypeChoices = {"DIGITAL", "ANALOGUE"};
                					String newProductChipType = (String) JOptionPane.showInputDialog(null, "Select New Chip Type", "Edit Existing Controller Product", JOptionPane.QUESTION_MESSAGE, null, chipTypeChoices, chipTypeChoices[0]);
                					if (newProductChipType == null) {
                						break;
                					}
                					PreparedStatement editProductChipTypestmt = connection.prepareStatement("UPDATE Controller SET chip_type = '" + newProductChipType+"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductChipTypestmt.executeUpdate();
                					dispose();
                					editControllers = new EditControllers();
                					editControllers.setLocationRelativeTo(null);
                					break;
                				case "Cost":
                        			String newProductCost = JOptionPane.showInputDialog(null, "Enter New Cost For Product", "Edit Existing Track Product", JOptionPane.INFORMATION_MESSAGE);
                        			if (newProductCost != null) {
                        				char[] newProductCostArray = newProductCost.toCharArray();
                            			for (char c : newProductCostArray) {
                            				if (Character.isDigit(c) == false) {
                            					newProductCost = null;
                            					JOptionPane.showMessageDialog(null, "Invalid Cost Value - Enter Only Numbers");
                            					break;
                            				}
                            			}
                            			if (newProductCost != null) {
                            				PreparedStatement editProductCoststmt = connection.prepareStatement("UPDATE Products SET retail_price = '" + newProductCost +"' WHERE product_code ='" + selectedValueProductCode + "'");
                            				editProductCoststmt.executeUpdate();
                            			}
                    					dispose();
                    					editControllers = new EditControllers();
                    					editControllers.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Stock":
                					String newProductStock = JOptionPane.showInputDialog(null, "Enter New Stock For Product", "Edit Existing Track Product", JOptionPane.INFORMATION_MESSAGE);
                        			if (newProductStock != null) {
                        				char[] newProductStockArray = newProductStock.toCharArray();
                            			for (char c : newProductStockArray) {
                            				if (Character.isDigit(c) == false) {
                            					newProductStock = null;
                            					JOptionPane.showMessageDialog(null, "Invalid Stock Value - Enter Only Numbers");
                            					break;
                            				}
                            			}
                            			if (newProductStock != null) {
                            				PreparedStatement editProductStockstmt = connection.prepareStatement("UPDATE Products SET stock = '" + newProductStock +"' WHERE product_code ='" + selectedValueProductCode + "'");
                            				editProductStockstmt.executeUpdate();
                            			}
                    					dispose();
                    					editControllers = new EditControllers();
                    					editControllers.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Gauge":
                					String[] gaugeChoices = {"OO", "TT", "N"};
                					String newGauge = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Track Product", JOptionPane.QUESTION_MESSAGE, null, gaugeChoices, gaugeChoices[0]);
                					if (newGauge == null) {
                						break;
                					}
                					PreparedStatement editProductGaugestmt = connection.prepareStatement("UPDATE Products SET gauge = '" + newGauge +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductGaugestmt.executeUpdate();
                					dispose();
                					editControllers = new EditControllers();
                					editControllers.setLocationRelativeTo(null);
                					break;
                				case "Scale":
                					String[] scaleChoices = {"1/76", "1/120", "1/148"};
                					String newScale = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Track Product", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
                					if (newScale == null) {
                						break;
                					}
                					PreparedStatement editProductScalestmt = connection.prepareStatement("UPDATE Products SET scale = '" + newScale +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductScalestmt.executeUpdate();
                					dispose();
                					editControllers = new EditControllers();
                					editControllers.setLocationRelativeTo(null);
                					break;
                				case "null":
                					
                				default:
                					break;
                				}
        					}

        				} catch (SQLException e2) {
        					e2.printStackTrace();
        				}
                	} else {
                		JOptionPane.showMessageDialog(null, "No product selected.");
                	}

                }
            });
            selectedProductDetailsPanel.add(editProductButton);
            
            //Create AddOrDelete Product Panel Items
            JButton addProductButton = new JButton("Add New Product");
            addProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newProductName = JOptionPane.showInputDialog(null, "Enter Product Name", "Add New Controller Product", JOptionPane.INFORMATION_MESSAGE);
                    if (newProductName != null && newProductName != "") {
                    	String newProductManufacturer = JOptionPane.showInputDialog(null, "Enter Manufacturer Name", "Add New Controller Product", JOptionPane.INFORMATION_MESSAGE);
                    	if (newProductManufacturer != null && newProductManufacturer != "") {
                    		String newProductRetailPrice = JOptionPane.showInputDialog(null, "Enter Retail Price (e.g: 100, 1.50, 2)", "Add New Track Product", JOptionPane.INFORMATION_MESSAGE);
                    		if (newProductRetailPrice != null && newProductRetailPrice != "") {
                    			char[] newProductRetailPriceArray = newProductRetailPrice.toCharArray();
                    			for (char c : newProductRetailPriceArray) {
                    				if (Character.isDigit(c) == false) {
                    					newProductRetailPrice = null;
                    					JOptionPane.showMessageDialog(null, "Invalid Retail Price - Enter Only Numbers");
                    					break;
                    				}
                    			}
                    			if (newProductRetailPrice != null) {
                        			String newProductStock = JOptionPane.showInputDialog(null, "Enter Current Stock", "Add New Track Product", JOptionPane.INFORMATION_MESSAGE);
                        			if (newProductStock != null && newProductStock != "") {
                        				char[] newProductStockArray = newProductStock.toCharArray();
                            			for (char c : newProductStockArray) {
                            				if (Character.isDigit(c) == false) {
                            					newProductStock = null;
                            					JOptionPane.showMessageDialog(null, "Invalid Stock Value - Enter Only Numbers");
                            					break;
                            				}
                            			}
                            			if (newProductStock != null) {
                            				String[] gaugeChoices = {"OO", "TT", "N"};
                            				String newProductGauge = (String) JOptionPane.showInputDialog(null, "Select Gauge", "Add New Track Product", JOptionPane.QUESTION_MESSAGE, null, gaugeChoices, gaugeChoices[0]);
                            				if (newProductGauge != null) {
                            					String[] scaleChoices = {"1/76", "1/120", "1/148"};
                                				String newProductScale = (String) JOptionPane.showInputDialog(null, "Select Scale", "Add New Track Product", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
                                				if (newProductScale != null) {
                                					
                                					
                                					
                                					String[] chipTypeChoices = {"ANALOGUE", "DIGITAL"};
                                					String newProductChipType = (String) JOptionPane.showInputDialog(null, "Select Chip Type", "Add New Controller Product", JOptionPane.QUESTION_MESSAGE, null, chipTypeChoices, chipTypeChoices[0]);
                                					if (newProductChipType != null) {
                                        					try {
                                        						String newProductCode = null;
                                        						PreparedStatement getProductCodesstmt = connection.prepareStatement("SELECT product_code FROM Products WHERE product_code LIKE 'C%'");
                                        						ResultSet productCodeResultSet = getProductCodesstmt.executeQuery();
                                        						List<String> existingProductCodes = new ArrayList<>();
                                        						while (productCodeResultSet.next()) {
                                        							existingProductCodes.add(productCodeResultSet.getString("product_code").substring(1));
                                        						}
                                        						for(int i=1; i<10000; i++) {
                                        							String itemToCheckFor = Integer.toString(i);
                                        							if (i < 10) {
                                        								itemToCheckFor = "00" + itemToCheckFor;
                                        							} else if (i > 9 && i < 100) {
                                        								itemToCheckFor = "0" + itemToCheckFor;
                                        							} else {
                                        								itemToCheckFor = Integer.toString(i);
                                        							}
                                        							
                                        							
                                        							if (existingProductCodes.contains(itemToCheckFor)) {
                                        								continue;
                                        							} else {
                                        								newProductCode = Integer.toString(i);
                                        								if (newProductCode.length() == 1) {
                                        									newProductCode = "00" + newProductCode;
                                        								} else if (newProductCode.length() == 2) {
                                        									newProductCode = "0" + newProductCode;
                                        								} else {
                                        									newProductCode = Integer.toString(i);
                                        								}
                                        								break;
                                        							}
                                        						}
                                        						
                                        						
                                        						
                                        						//CREATE NEW RECORD WITH GIVEN DETAILS (PRODUCT CODE NEEDS TO START WITH R)
                                        						PreparedStatement createNewProductstmt = connection.prepareStatement("INSERT INTO Products (product_code, product_name, manufacturer_name, retail_price, stock, gauge, scale) VALUES (?, ?, ?, ?, ?, ?, ?)");
                                        						createNewProductstmt.setString(1, "C" + newProductCode);
                                        						createNewProductstmt.setString(2, newProductName);
                                        						createNewProductstmt.setString(3, newProductManufacturer);
                                        						createNewProductstmt.setString(4, newProductRetailPrice);
                                        						createNewProductstmt.setString(5, newProductStock);
                                        						createNewProductstmt.setString(6, newProductGauge);
                                        						createNewProductstmt.setString(7, newProductScale);
        														int rowsUpdated = createNewProductstmt.executeUpdate();
        							                			if (rowsUpdated == 1) {
        							                				PreparedStatement createNewControllerstmt = connection.prepareStatement("INSERT INTO Controller (product_code, chip_type) VALUES (?, ?)");
        							                				createNewControllerstmt.setString(1, "C" + newProductCode);
        							                				createNewControllerstmt.setString(2, newProductChipType);
        							                				createNewControllerstmt.executeUpdate();
        							    			                System.out.println("Product successfully added.");
        							    			                ControllerVector.clear();
        							    			                productInformationVector.clear();
        							    			                controllerList.clearSelection();
        							    			                productInformationList.clearSelection();
        							    			                getAllControllerstmt = connection.createStatement();
        							    			                //Get All Track Products
        							    			                ControllerListResultSet = getAllControllerstmt.executeQuery("SELECT * FROM Products INNER JOIN Controller ON Products.product_code = Controller.product_code");
        							    			                
        							    			                while (ControllerListResultSet.next()) {
        							    			                    String row = ControllerListResultSet.getString("product_code") + ", " + ControllerListResultSet.getString("chip_type");
        							    			                    String productRow = ControllerListResultSet.getString("product_name") + ", £" + ControllerListResultSet.getString("retail_price") + ", " + ControllerListResultSet.getString("stock") + ", " + ControllerListResultSet.getString("gauge") + ", " + ControllerListResultSet.getString("scale");
        							    			                    productInformationVector.add(productRow);
        							    			                    ControllerVector.add(row);
        							    			                }
        							    			                
        							    			                
        							    			                
        							    			                JList<String> controllerList = new JList<String>(ControllerVector);
        							    			                JList<String> productInformationList = new JList<String>(productInformationVector);
        							    			                controllerList.setSelectedIndex(0);
        							    			                selectedValue = controllerList.getSelectedValue();
        							    	                        productInformationList.setSelectedIndex(0);
        							    	                        selectedValueInformation = productInformationList.getSelectedValue();
        							    	                        
        							    			                if (selectedValue != null) {
        							    			                    int firstCommaIndex = selectedValue.indexOf(",");
        							    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
        							    			                    selectedValueChipType = selectedValue.substring(firstCommaIndex + 2,  selectedValue.length());
        							    			                    
        							    			                    
        							    			                    int firstCommaIndex3 = selectedValueInformation.indexOf(",");
        							    			                    selectedValueName = selectedValueInformation.substring(0, firstCommaIndex3);
        							    			                    int secondCommaIndex3 = selectedValueInformation.indexOf(",", firstCommaIndex3 + 1);
        							    			                    selectedValueCost = selectedValueInformation.substring(firstCommaIndex3 + 3, secondCommaIndex3);
        							    			                    int thirdCommaIndex3 = selectedValueInformation.indexOf(", ", secondCommaIndex3 + 1);
        							    			                    selectedValueStock = selectedValueInformation.substring(secondCommaIndex3 + 2, thirdCommaIndex3);
        							    			                    int fourthCommaIndex3 = selectedValueInformation.indexOf(", ", thirdCommaIndex3 + 1);
        							    			                    selectedValueGauge = selectedValueInformation.substring(thirdCommaIndex3 + 2, fourthCommaIndex3);
        							    			                    selectedValueScale = selectedValueInformation.substring(fourthCommaIndex3 + 2, selectedValueInformation.length());
        							    			                    
        							    			                    
        							    			                    
        							    			                    
        							    			                    //DISPLAYING SELECTED TRACK DETAILS
        							    			                    productName.setText("Product Name: " + selectedValueName);
        							    			                    productCode.setText("Product Code: " + selectedValueProductCode);
        							    			                    chipType.setText("Control Type: " + selectedValueChipType);
        							    			                    productCost.setText("Product Cost: £" + selectedValueCost);
        							    			                    productStock.setText("Product Stock: " + selectedValueStock);
        							    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
        							    			                    productScale.setText("Product Scale: " + selectedValueScale);
        							    			                    JOptionPane.showMessageDialog(null, "Successfully added new product.");
        							    				                
        							    				                repaint();
        							    			                } else {
        							    			                	productCode.setText("Product Code: ");
        							    			                	chipType.setText("Control Type: ");
        							    			                    productCost.setText("Product Cost: £");
        							    			                    productGauge.setText("Product Gauge: ");
        							    			                    productScale.setText("Product Scale: ");
        							    			                	repaint();
        							    			                }
        							            					dispose();
        							            					EditControllers editControllers = new EditControllers();
        							            					editControllers.setLocationRelativeTo(null);
        							                			}
        														
        														
        													} catch (SQLException e1) {
        														e1.printStackTrace();
        													}
                                        					
                                  
                                						}
                                					}
                                				}
                            				}
                            			}
                        			}
                    			}
                    		}
                    	}
                    }
            });
            
            
            
            
            
            
            
            
            
            JButton deleteProductButton = new JButton("Delete Selected Product");
            deleteProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (selectedValueProductCode != null) {
                		try {
							PreparedStatement deleteControllerstmt = connection.prepareStatement("DELETE FROM Controller WHERE product_code='" + selectedValueProductCode + "'");
							PreparedStatement deleteProductstmt = connection.prepareStatement("DELETE FROM Products WHERE product_code='" + selectedValueProductCode + "'");
                			int rowsUpdated = deleteControllerstmt.executeUpdate();
                			deleteProductstmt.executeUpdate();
                			if (rowsUpdated == 1) {
    			                System.out.println("Product successfully deleted.");
    			                ControllerVector.clear();
    			                productInformationVector.clear();
    			                controllerList.clearSelection();
    			                productInformationList.clearSelection();
    			                getAllControllerstmt = connection.createStatement();
    			                //Get All Track Products
    			                ControllerListResultSet = getAllControllerstmt.executeQuery("SELECT * FROM Products INNER JOIN Controller ON Products.product_code = Controller.product_code");
    			                
    			                while (ControllerListResultSet.next()) {
    			                    String row = ControllerListResultSet.getString("product_code") + ", " + ControllerListResultSet.getString("chip_type");
    			                    String productRow = ControllerListResultSet.getString("product_name") + ", £" + ControllerListResultSet.getString("retail_price") + ", " + ControllerListResultSet.getString("stock") + ", " + ControllerListResultSet.getString("gauge") + ", " + ControllerListResultSet.getString("scale");
    			                    productInformationVector.add(productRow);
    			                    ControllerVector.add(row);
    			                }
    			                JList<String> controllerList = new JList<String>(ControllerVector);
    			                controllerList.setSelectedIndex(0);
    			                selectedValue = controllerList.getSelectedValue();
    	                        productInformationList.setSelectedIndex(0);
    	                        selectedValueInformation = productInformationList.getSelectedValue();
    			                if (selectedValue != null) {
    			                    int firstCommaIndex = selectedValue.indexOf(",");
    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
    			                    selectedValueChipType = selectedValue.substring(firstCommaIndex + 2, selectedValue.length());
    		
    			                    
    			                    int firstCommaIndex3 = selectedValueInformation.indexOf(",");
    			                    selectedValueName = selectedValueInformation.substring(0, firstCommaIndex3);
    			                    int secondCommaIndex3 = selectedValueInformation.indexOf(",", firstCommaIndex3 + 1);
    			                    selectedValueCost = selectedValueInformation.substring(firstCommaIndex3 + 3, secondCommaIndex3);
    			                    int thirdCommaIndex3 = selectedValueInformation.indexOf(", ", secondCommaIndex3 + 1);
    			                    selectedValueStock = selectedValueInformation.substring(secondCommaIndex3 + 2, thirdCommaIndex3);
    			                    int fourthCommaIndex3 = selectedValueInformation.indexOf(", ", thirdCommaIndex3 + 1);
    			                    selectedValueGauge = selectedValueInformation.substring(thirdCommaIndex3 + 2, fourthCommaIndex3);
    			                    selectedValueScale = selectedValueInformation.substring(fourthCommaIndex3 + 2, selectedValueInformation.length());
    			                    
    			                    
    			                    
    			                    
    			                    //DISPLAYING SELECTED TRACK DETAILS
    			                    productName.setText("Product Name: " + selectedValueName);
    			                    productCode.setText("Product Code: " + selectedValueProductCode);
    			                    chipType.setText("Control Type: " + selectedValueChipType);
    			                    productCost.setText("Product Cost: " + selectedValueCost);
    			                    productStock.setText("Product Stock: " + selectedValueStock);
    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
    			                    productScale.setText("Product Scale: " + selectedValueScale);
    				                
    				                
    				                repaint();
    			                } else {
    			                	productCode.setText("Product Code: ");
    			                	chipType.setText("Control Type: ");
    			                    productCost.setText("Product Cost: ");
    			                    productGauge.setText("Product Gauge: ");
    			                    productScale.setText("Product Scale: ");
    			                	repaint();
    			                }
    			                JOptionPane.showMessageDialog(null, "Successfully deleted product.");

                			}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
                	} else {
                		repaint();
                		JOptionPane.showMessageDialog(null, "No product currently selected.");
                	}
                }
            });
            

            
            
            

            

            
            if (selectedValue != null) {
                int firstCommaIndex = selectedValue.indexOf(",");
                selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
                selectedValueChipType = selectedValue.substring(firstCommaIndex + 2, selectedValue.length());

                
                int firstCommaIndex3 = selectedValueInformation.indexOf(",");
                selectedValueName = selectedValueInformation.substring(0, firstCommaIndex3);
                int secondCommaIndex3 = selectedValueInformation.indexOf(",", firstCommaIndex3 + 1);
                selectedValueCost = selectedValueInformation.substring(firstCommaIndex3 + 3, secondCommaIndex3);
                int thirdCommaIndex3 = selectedValueInformation.indexOf(", ", secondCommaIndex3 + 1);
                selectedValueStock = selectedValueInformation.substring(secondCommaIndex3 + 2, thirdCommaIndex3);
                int fourthCommaIndex3 = selectedValueInformation.indexOf(", ", thirdCommaIndex3 + 1);
                selectedValueGauge = selectedValueInformation.substring(thirdCommaIndex3 + 2, fourthCommaIndex3);
                selectedValueScale = selectedValueInformation.substring(fourthCommaIndex3 + 2, selectedValueInformation.length());
                
                //DISPLAYING SELECTED TRACK DETAILS
                productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: " + selectedValueProductCode);
                chipType.setText("Control Type: " + selectedValueChipType);
                productCost.setText("Product Cost: " + selectedValueCost);
                productStock.setText("Product Stock: " + selectedValueStock);
                productGauge.setText("Product Gauge: " + selectedValueGauge);
                productScale.setText("Product Scale: " + selectedValueScale);
            } else {
            	productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: ");
                chipType.setText("Control Type: ");
                productCost.setText("Product Cost: ");
                productGauge.setText("Product Gauge: ");
                productScale.setText("Product Scale: ");
            }
            

            
            
            controllerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            controllerList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedValue = controllerList.getSelectedValue();
                        selectedIndex = controllerList.getSelectedIndex();
                        productInformationList.setSelectedIndex(selectedIndex);
                        selectedValueInformation = productInformationList.getSelectedValue();
                        if (selectedValue != null) {
                            int firstCommaIndex2 = selectedValue.indexOf(",");
                            selectedValueProductCode = selectedValue.substring(0, firstCommaIndex2);
                            selectedValueChipType = selectedValue.substring(firstCommaIndex2 + 2, selectedValue.length());
                             
                            int firstCommaIndex3 = selectedValueInformation.indexOf(",");
                            selectedValueName = selectedValueInformation.substring(0, firstCommaIndex3);
                            int secondCommaIndex3 = selectedValueInformation.indexOf(",", firstCommaIndex3 + 1);
                            selectedValueCost = selectedValueInformation.substring(firstCommaIndex3 + 3, secondCommaIndex3);
                            int thirdCommaIndex3 = selectedValueInformation.indexOf(", ", secondCommaIndex3 + 1);
                            selectedValueStock = selectedValueInformation.substring(secondCommaIndex3 + 2, thirdCommaIndex3);
                            int fourthCommaIndex3 = selectedValueInformation.indexOf(", ", thirdCommaIndex3 + 1);
                            selectedValueGauge = selectedValueInformation.substring(thirdCommaIndex3 + 2, fourthCommaIndex3);
                            selectedValueScale = selectedValueInformation.substring(fourthCommaIndex3 + 2, selectedValueInformation.length());
                            
                            //New Label Values
                            productName.setText("Product Name: " + selectedValueName);
                            productCode.setText("Product Code: " + selectedValueProductCode);
                            chipType.setText("Control Type: " + selectedValueChipType);
                            productCost.setText("Product Cost: " + selectedValueCost);
                            productStock.setText("Product Stock: " + selectedValueStock);
                            productGauge.setText("Product Gauge: " + selectedValueGauge);
                            productScale.setText("Product Scale: " + selectedValueScale);
   
                            
                        }
                    }
                }
            });
            


            
            //Add List to Scrollable Pane
            JScrollPane staffScrollableList = new JScrollPane(controllerList);
            //Add Items to Demote User Panel 
            tracksListPanel.add(controllerListLabel, BorderLayout.NORTH);
            tracksListPanel.add(staffScrollableList, BorderLayout.WEST);
            //Add Items to AddOrDelete Product Panel
            addOrDeletePanel.add(addProductButton);
            addOrDeletePanel.add(deleteProductButton);
            
            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    
    