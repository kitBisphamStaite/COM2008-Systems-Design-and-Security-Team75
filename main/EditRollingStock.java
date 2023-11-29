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

public class EditRollingStock extends JFrame {	
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
    Connection connection;
    //SQL
    Statement getAllRollingStockstmt;
    ResultSet rollingStockListResultSet;
    Vector<String> rollingStockVector = new Vector<String>();
    //SelectedValue from List
	String selectedValue;
	int selectedIndex;
	String selectedValueProductCode;
	JList<String> rollingStockList = new JList<String>();
	String selectedValueEraCode;
	String selectedValueInformation;
	String selectedValueCost;
	String selectedValueName;
	String selectedValueStock;
	String selectedValueGauge;
	String selectedValueScale;
	
    public EditRollingStock() {
        //GET LIST OF LOCOMOTIVES
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            getAllRollingStockstmt = connection.createStatement();
            //Get All Locomotive Products
            rollingStockListResultSet = getAllRollingStockstmt.executeQuery("SELECT * FROM Products INNER JOIN Rolling_Stock ON Products.product_code = Rolling_Stock.product_code");
            
            while (rollingStockListResultSet.next()) {
                String row = rollingStockListResultSet.getString("product_code") + "," + rollingStockListResultSet.getString("product_name") + ", £" + rollingStockListResultSet.getString("retail_price") + ", " + rollingStockListResultSet.getString("stock") + ", " + rollingStockListResultSet.getString("gauge") + ", " + rollingStockListResultSet.getString("scale") + "," + rollingStockListResultSet.getString("era_code");
                rollingStockVector.add(row);
            }
  
            //Set Up Frame
            setTitle("Edit Rolling Stock");
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
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Rolling Stock");
            trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
            //Add Items To Header Panel
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
            
            //Create Panels
            JPanel rollingStockListPanel = new JPanel(new BorderLayout());
            JPanel addOrDeletePanel = new JPanel(new GridLayout(0,2));
            JPanel selectedProductDetailsPanel = new JPanel(new GridLayout(15,0,0,0));
            //Add Panels To Frame
            add(headerPanel, BorderLayout.NORTH);
            add(rollingStockListPanel, BorderLayout.CENTER);
            add(addOrDeletePanel, BorderLayout.SOUTH);
            rollingStockListPanel.add(selectedProductDetailsPanel, BorderLayout.CENTER);
            
            
            
            
            
            
            //Create Locomotive List Panel Items
            JLabel rollingStockListLabel = new JLabel("Rolling Stock Product List:");
            JList<String> rollingStockList = new JList<String>(rollingStockVector);
            rollingStockList.setPreferredSize(new Dimension(200, 200));
            rollingStockList.setSelectedIndex(0);
            selectedIndex = 0;
            selectedValue = rollingStockList.getSelectedValue();
            JLabel productInfo = new JLabel("Selected Rolling Stock Product Info:");
            productInfo.setFont(new Font("Dialog", Font.BOLD, 22));
            selectedProductDetailsPanel.add(productInfo);
            JLabel productName = new JLabel("Product Name: ");
            productName.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productName);
            JLabel productCode = new JLabel("Product Code: ");
            productCode.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productCode);
            JLabel eraCode = new JLabel("Product Era Code: ");
            eraCode.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(eraCode);
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
                    	String[] detailToEditChoices = {"Name", "ERA Code", "Cost", "Stock", "Gauge", "Scale"};
        				String detailToEdit = (String) JOptionPane.showInputDialog(null, "Select Detail To Edit", "Edit Existing Rolling Stock", JOptionPane.QUESTION_MESSAGE, null, detailToEditChoices, detailToEditChoices[0]);
        				try {
        					EditRollingStock editRollingStock;
        					String selectedProduct = rollingStockList.getSelectedValue();
        					selectedValueProductCode = selectedValue.substring(0, selectedValue.indexOf(","));
        					if (detailToEdit != null) {
                				switch(detailToEdit) {
                				case "Name":
                					String newProductName = JOptionPane.showInputDialog(null, "Enter New Name For Product", "Edit Existing Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
                					if (newProductName == null) {
                						break;
                					}
                					PreparedStatement editProductNamestmt = connection.prepareStatement("UPDATE Products SET product_name = '" + newProductName +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductNamestmt.executeUpdate();
                					dispose();
                					editRollingStock = new EditRollingStock();
                					editRollingStock.setLocationRelativeTo(null);
                					break;
                				case "ERA Code":
                					String newProductEraCode = JOptionPane.showInputDialog(null, "Enter New ERA Code For Product", "Edit Existing Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
                					if (newProductEraCode == null) {
                						break;
                					}
                					PreparedStatement editProductEraCodestmt = connection.prepareStatement("UPDATE Rolling_Stock SET era_code = '" + newProductEraCode +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductEraCodestmt.executeUpdate();
                					dispose();
                					editRollingStock = new EditRollingStock();
                					editRollingStock.setLocationRelativeTo(null);
                					break;
                				case "Cost":
                        			String newProductCost = JOptionPane.showInputDialog(null, "Enter New Cost For Product", "Edit Existing Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
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
                    					editRollingStock = new EditRollingStock();
                    					editRollingStock.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Stock":
                					String newProductStock = JOptionPane.showInputDialog(null, "Enter New Stock For Product", "Edit Existing Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
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
                    					editRollingStock = new EditRollingStock();
                    					editRollingStock.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Gauge":
                					String[] gaugeChoices = {"OO", "TT", "N"};
                					String newGauge = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Rolling Stock", JOptionPane.QUESTION_MESSAGE, null, gaugeChoices, gaugeChoices[0]);
                					if (newGauge == null) {
                						break;
                					}
                					switch (newGauge) {
                					case "OO":
                						newGauge = "1";
                						break;
                					case "TT":
                						newGauge = "2";
                						break;
                					case "N":
                						newGauge = "3";
                						break;
                					}
                					PreparedStatement editProductGaugestmt = connection.prepareStatement("UPDATE Products SET gauge = '" + newGauge +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductGaugestmt.executeUpdate();
                					dispose();
                					editRollingStock = new EditRollingStock();
                					editRollingStock.setLocationRelativeTo(null);
                					break;
                				case "Scale":
                					String[] scaleChoices = {"1/76", "1/120", "1/148"};
                					String newScale = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Rolling Stock", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
                					if (newScale == null) {
                						break;
                					}
                					PreparedStatement editProductScalestmt = connection.prepareStatement("UPDATE Products SET scale = '" + newScale +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductScalestmt.executeUpdate();
                					dispose();
                					editRollingStock = new EditRollingStock();
                					editRollingStock.setLocationRelativeTo(null);
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
                    String newProductName = JOptionPane.showInputDialog(null, "Enter Product Name", "Add New Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
                    if (newProductName != null && newProductName != "") {
                    	String newProductManufacturer = JOptionPane.showInputDialog(null, "Enter Manufacturer Name", "Add New Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
                    	if (newProductManufacturer != null && newProductManufacturer != "") {
                    		String newProductRetailPrice = JOptionPane.showInputDialog(null, "Enter Retail Price (e.g: 100, 1.50, 2)", "Add New Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
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
                        			String newProductStock = JOptionPane.showInputDialog(null, "Enter Current Stock", "Add New Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
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
                            				String newProductGauge = (String) JOptionPane.showInputDialog(null, "Select Gauge", "Add New Rolling Stock", JOptionPane.QUESTION_MESSAGE, null, gaugeChoices, gaugeChoices[0]);
                            				if (newProductGauge != null) {
                            					switch (newProductGauge) {
                            					case "OO":
                            						newProductGauge = "1";
                            						break;
                            					case "TT":
                            						newProductGauge = "2";
                            						break;
                            					case "N":
                            						newProductGauge = "3";
                            						break;
                            					}
                            					String[] scaleChoices = {"1/76", "1/120", "1/148"};
                                				String newProductScale = (String) JOptionPane.showInputDialog(null, "Select Scale", "Add New Rolling Stock", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
                                				if (newProductScale != null) {
                                					switch (newProductScale) {
                                					case "1/76":
                                						newProductScale = "1";
                                						break;
                                					case "1/120":
                                						newProductScale = "2";
                                						break;
                                					case "1/148":
                                						newProductScale = "3";
                                						break;
                                					}
                                					String[] controlTypeChoices = {"ANALOGUE", "DCC-READY", "DCC-FITTED", "DCC-SOUND"};
                                					String newProductControlType = (String) JOptionPane.showInputDialog(null, "Select Control Type", "Add New Rolling Stock", JOptionPane.QUESTION_MESSAGE, null, controlTypeChoices, controlTypeChoices[0]);
                                					if (newProductControlType != null) {
                                						switch (newProductControlType) {
                                						case "ANALOGUE":
                                							newProductControlType = "1";
                                							break;
                                						case "DCC-READY":
                                							newProductControlType = "2";
                                							break;
                                						case "DCC-FITTED":
                                							newProductControlType = "3";
                                							break;
                                						case "DCC-SOUND":
                                							newProductControlType = "4";
                                							break;
                                						}
                                						String newProductEraCode = JOptionPane.showInputDialog(null, "Enter Era Code", "Add New Rolling Stock", JOptionPane.INFORMATION_MESSAGE);
                                						if (newProductEraCode != null && newProductEraCode != "") {
                                        					try {
                                        						String newProductCode = null;
                                        						PreparedStatement getProductCodesstmt = connection.prepareStatement("SELECT product_code FROM Products WHERE product_code LIKE 'S%'");
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
                                        						
                                        						
                                        						
                                        						//CREATE NEW RECORD WITH GIVEN DETAILS (PRODUCT CODE NEEDS TO START WITH S)
                                        						PreparedStatement createNewProductstmt = connection.prepareStatement("INSERT INTO Products (product_code, product_name, manufacturer_name, retail_price, stock, gauge, scale) VALUES (?, ?, ?, ?, ?, ?, ?)");
                                        						createNewProductstmt.setString(1, "S" + newProductCode);
                                        						createNewProductstmt.setString(2, newProductName);
                                        						createNewProductstmt.setString(3, newProductManufacturer);
                                        						createNewProductstmt.setString(4, newProductRetailPrice);
                                        						createNewProductstmt.setString(5, newProductStock);
                                        						createNewProductstmt.setString(6, newProductGauge);
                                        						createNewProductstmt.setString(7, newProductScale);
        														int rowsUpdated = createNewProductstmt.executeUpdate();
        							                			if (rowsUpdated == 1) {
        							                				PreparedStatement createNewLocomotivestmt = connection.prepareStatement("INSERT INTO Rolling_Stock (product_code, era_code) VALUES (?, ?)");
        							                				createNewLocomotivestmt.setString(1, "S" + newProductCode);
        							                				createNewLocomotivestmt.setString(3, newProductEraCode);
        							                				createNewLocomotivestmt.executeUpdate();
        							    			                System.out.println("Product successfully added.");
        							    			                rollingStockVector.clear();
        							    			                rollingStockList.clearSelection();
        							    			                getAllRollingStockstmt = connection.createStatement();
        							    			                //Get All Locomotive Products
        							    			                rollingStockListResultSet = getAllRollingStockstmt.executeQuery("SELECT * FROM Products INNER JOIN Rolling_Stock ON Products.product_code = Rolling_Stock.product_code");
        							    			                
        							    			                while (rollingStockListResultSet.next()) {
        							    			                    String row = rollingStockListResultSet.getString("product_code") + "," + rollingStockListResultSet.getString("product_name") + ", £" + rollingStockListResultSet.getString("retail_price") + ", " + rollingStockListResultSet.getString("stock") + ", " + rollingStockListResultSet.getString("gauge") + ", " + rollingStockListResultSet.getString("scale") + "," + rollingStockListResultSet.getString("era_code");
        							    			                    rollingStockVector.add(row);
        							    			                }
        							    			                
        							    			                
        							    			                
        							    			                JList<String> rollingStockList = new JList<String>(rollingStockVector);
        							    			                rollingStockList.setSelectedIndex(0);
        							    			                selectedValue = rollingStockList.getSelectedValue();
        							    	                        
        							    	                        
        							    	                        
        							    			                if (selectedValue != null) {
        							    			                    int firstCommaIndex = selectedValue.indexOf(",");
        							    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
        							    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
        							    			                    selectedValueName = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
        							    			                    int thirdCommaIndex = selectedValue.indexOf(",", secondCommaIndex + 1);
        							    			                    selectedValueCost = selectedValue.substring(secondCommaIndex + 3, thirdCommaIndex);
        							    			                    int fourthCommaIndex = selectedValue.indexOf(",", thirdCommaIndex + 1);
        							    			                    selectedValueStock = selectedValue.substring(thirdCommaIndex + 2, fourthCommaIndex);
        							    			                    int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
        							    			                    selectedValueGauge = selectedValue.substring(fourthCommaIndex + 2, fifthCommaIndex);
        							    			                    int sixthCommaIndex = selectedValue.indexOf(",", fifthCommaIndex + 1);
        							    			                    selectedValueScale = selectedValue.substring(fifthCommaIndex + 2, sixthCommaIndex);
        							    			                    selectedValueEraCode = selectedValue.substring(sixthCommaIndex +2, selectedValue.length());
        							    			                    //DISPLAYING SELECTED ROLLING STOCK DETAILS
        							    			                    productName.setText("Product Name: " + selectedValueName);
        							    			                    productCode.setText("Product Code: " + selectedValueProductCode);
        							    			                    eraCode.setText("Era Code: " + selectedValueEraCode);
        							    			                    productCost.setText("Product Cost: £" + selectedValueCost);
        							    			                    productStock.setText("Product Stock: " + selectedValueStock);
        							    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
        							    			                    productScale.setText("Product Scale: " + selectedValueScale);
        							    				                
        							    				                
        							    				                repaint();
        							    			                } else {
        							    			                	productName.setText("Product Name: ");
        							    			                	productCode.setText("Product Code: ");
        							    			                	eraCode.setText("Era Code: ");
        							    			                    productCost.setText("Product Cost: £");
        							    			                    productGauge.setText("Product Gauge: ");
        							    			                    productScale.setText("Product Scale: ");
        							    			                	repaint();
        							    			                }
        							    			                JOptionPane.showMessageDialog(null, "Successfully added new product.");
        							            					dispose();
        							            					EditLocomotives editLocomotives = new EditLocomotives();
        							            					editLocomotives.setLocationRelativeTo(null);
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
                }
            });
            
            
            
            
            
            
            
            
            
            JButton deleteProductButton = new JButton("Delete Selected Product");
            deleteProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (selectedValueProductCode != null) {
                		try {
							PreparedStatement deleteRollingStockstmt = connection.prepareStatement("DELETE FROM Rolling_Stock WHERE product_code='" + selectedValueProductCode + "'");
							PreparedStatement deleteProductstmt = connection.prepareStatement("DELETE FROM Products WHERE product_code='" + selectedValueProductCode + "'");
                			int rowsUpdated = deleteRollingStockstmt.executeUpdate();
                			deleteProductstmt.executeUpdate();
                			if (rowsUpdated == 1) {
    			                System.out.println("Product successfully deleted.");
    			                rollingStockVector.clear();
    			                rollingStockList.clearSelection();
    			                getAllRollingStockstmt = connection.createStatement();
    			                //Get All Locomotive Products
    			                rollingStockListResultSet = getAllRollingStockstmt.executeQuery("SELECT * FROM Products INNER JOIN Rolling_Stock ON Products.product_code = Rolling_Stock.product_code");
    			                
    			                while (rollingStockListResultSet.next()) {
    			                    String row = rollingStockListResultSet.getString("product_code") + "," + rollingStockListResultSet.getString("product_name") + ", £" + rollingStockListResultSet.getString("retail_price") + ", " + rollingStockListResultSet.getString("stock") + ", " + rollingStockListResultSet.getString("gauge") + ", " + rollingStockListResultSet.getString("scale") + "," + rollingStockListResultSet.getString("era_code");
    			                    rollingStockVector.add(row);
    			                }
    			                JList<String> rollingStockList = new JList<String>(rollingStockVector);
    			                rollingStockList.setSelectedIndex(0);
    			                selectedValue = rollingStockList.getSelectedValue();
    			                if (selectedValue != null) {
	    			                    int firstCommaIndex = selectedValue.indexOf(",");
	    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
	    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
	    			                    selectedValueName = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
	    			                    int thirdCommaIndex = selectedValue.indexOf(",", secondCommaIndex + 1);
	    			                    selectedValueCost = selectedValue.substring(secondCommaIndex + 3, thirdCommaIndex);
	    			                    int fourthCommaIndex = selectedValue.indexOf(",", thirdCommaIndex + 1);
	    			                    selectedValueStock = selectedValue.substring(thirdCommaIndex + 2, fourthCommaIndex);
	    			                    int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
	    			                    selectedValueGauge = selectedValue.substring(fourthCommaIndex + 2, fifthCommaIndex);
	    			                    int sixthCommaIndex = selectedValue.indexOf(",", fifthCommaIndex + 1);
	    			                    selectedValueScale = selectedValue.substring(fifthCommaIndex + 2, sixthCommaIndex);
	    			                    selectedValueEraCode = selectedValue.substring(sixthCommaIndex +2, selectedValue.length());
	    			                    //DISPLAYING SELECTED ROLLING STOCK DETAILS
	    			                    productName.setText("Product Name: " + selectedValueName);
	    			                    productCode.setText("Product Code: " + selectedValueProductCode);
	    			                    eraCode.setText("Era Code: " + selectedValueEraCode);
	    			                    productCost.setText("Product Cost: £" + selectedValueCost);
	    			                    productStock.setText("Product Stock: " + selectedValueStock);
	    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
	    			                    productScale.setText("Product Scale: " + selectedValueScale);
    				                
    				                
    				                repaint();
    			                } else {
    			                	productCode.setText("Product Code: ");
    			                	eraCode.setText("Era Code: ");
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
                int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
                selectedValueName = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
                int thirdCommaIndex = selectedValue.indexOf(",", secondCommaIndex + 1);
                selectedValueCost = selectedValue.substring(secondCommaIndex + 3, thirdCommaIndex);
                int fourthCommaIndex = selectedValue.indexOf(",", thirdCommaIndex + 1);
                selectedValueStock = selectedValue.substring(thirdCommaIndex + 2, fourthCommaIndex);
                int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
                selectedValueGauge = selectedValue.substring(fourthCommaIndex + 2, fifthCommaIndex);
                int sixthCommaIndex = selectedValue.indexOf(",", fifthCommaIndex + 1);
                selectedValueScale = selectedValue.substring(fifthCommaIndex + 2, sixthCommaIndex);
                selectedValueEraCode = selectedValue.substring(sixthCommaIndex +2, selectedValue.length());
                //DISPLAYING SELECTED ROLLING STOCK DETAILS
                productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: " + selectedValueProductCode);
                eraCode.setText("Era Code: " + selectedValueEraCode);
                productCost.setText("Product Cost: £" + selectedValueCost);
                productStock.setText("Product Stock: " + selectedValueStock);
                productGauge.setText("Product Gauge: " + selectedValueGauge);
                productScale.setText("Product Scale: " + selectedValueScale);
                repaint();
            } else {
            	productName.setText("Product Name: ");
                productCode.setText("Product Code: ");
                eraCode.setText("Era Code: ");
                productCost.setText("Product Cost: ");
                productGauge.setText("Product Gauge: ");
                productScale.setText("Product Scale: ");
            }
            

            
            
            rollingStockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            rollingStockList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedValue = rollingStockList.getSelectedValue();
                        selectedIndex = rollingStockList.getSelectedIndex();
                        if (selectedValue != null) {
		                    int firstCommaIndex = selectedValue.indexOf(",");
		                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
		                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
		                    selectedValueName = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
		                    int thirdCommaIndex = selectedValue.indexOf(",", secondCommaIndex + 1);
		                    selectedValueCost = selectedValue.substring(secondCommaIndex + 3, thirdCommaIndex);
		                    int fourthCommaIndex = selectedValue.indexOf(",", thirdCommaIndex + 1);
		                    selectedValueStock = selectedValue.substring(thirdCommaIndex + 2, fourthCommaIndex);
		                    int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
		                    selectedValueGauge = selectedValue.substring(fourthCommaIndex + 2, fifthCommaIndex);
		                    int sixthCommaIndex = selectedValue.indexOf(",", fifthCommaIndex + 1);
		                    selectedValueScale = selectedValue.substring(fifthCommaIndex + 2, sixthCommaIndex);
		                    selectedValueEraCode = selectedValue.substring(sixthCommaIndex +2, selectedValue.length());
		                    //DISPLAYING SELECTED ROLLING STOCK DETAILS
		                    productName.setText("Product Name: " + selectedValueName);
		                    productCode.setText("Product Code: " + selectedValueProductCode);
		                    eraCode.setText("Era Code: " + selectedValueEraCode);
		                    productCost.setText("Product Cost: £" + selectedValueCost);
		                    productStock.setText("Product Stock: " + selectedValueStock);
		                    productGauge.setText("Product Gauge: " + selectedValueGauge);
		                    productScale.setText("Product Scale: " + selectedValueScale);
                        }
                    }
                }
            });
            


            
            //Add List to Scrollable Pane
            JScrollPane rollingStockScrollableList = new JScrollPane(rollingStockList);
            //Add Items to Demote User Panel 
            rollingStockListPanel.add(rollingStockListLabel, BorderLayout.NORTH);
            rollingStockListPanel.add(rollingStockScrollableList, BorderLayout.WEST);
            //Add Items to AddOrDelete Product Panel
            addOrDeletePanel.add(addProductButton);
            addOrDeletePanel.add(deleteProductButton);
            
            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    
    