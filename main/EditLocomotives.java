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

public class EditLocomotives extends JFrame {	
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
    Connection connection;
    //SQL
    Statement getAllLocomotivestmt;
    ResultSet locomotiveListResultSet;
    Vector<String> locomotiveVector = new Vector<String>();
    Vector<String> productInformationVector = new Vector<String>();
    //SelectedValue from List
	String selectedValue;
	int selectedIndex;
	String selectedValueProductCode;
	JList<String> locomotiveList = new JList<String>();
	String selectedValueControlType;
	String selectedValueEraCode;
	String selectedValueInformation;
	String selectedValueCost;
	String selectedValueName;
	String selectedValueStock;
	String selectedValueGauge;
	String selectedValueScale;
	
    public EditLocomotives() {
        //GET LIST OF LOCOMOTIVES
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            getAllLocomotivestmt = connection.createStatement();
            //Get All Locomotive Products
            locomotiveListResultSet = getAllLocomotivestmt.executeQuery("SELECT * FROM Products INNER JOIN Locomotive ON Products.product_code = Locomotive.product_code");
            
            while (locomotiveListResultSet.next()) {
                String row = locomotiveListResultSet.getString("product_code") + ", " + locomotiveListResultSet.getString("product_name") + ", " + locomotiveListResultSet.getString("eraCode");
                String productRow = locomotiveListResultSet.getString("product_name") + ", £" + locomotiveListResultSet.getString("retail_price") + ", " + locomotiveListResultSet.getString("stock") + ", " + locomotiveListResultSet.getString("gauge") + ", " + locomotiveListResultSet.getString("scale");
                productInformationVector.add(productRow);
                locomotiveVector.add(row);
            }
  
            //Set Up Frame
            setTitle("Edit Locomotives");
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
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Locomotive Products");
            trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
            //Add Items To Header Panel
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
            
            //Create Panels
            JPanel locomotivesListPanel = new JPanel(new BorderLayout());
            JPanel addOrDeletePanel = new JPanel(new GridLayout(0,2));
            JPanel selectedProductDetailsPanel = new JPanel(new GridLayout(15,0,0,0));
            //Add Panels To Frame
            add(headerPanel, BorderLayout.NORTH);
            add(locomotivesListPanel, BorderLayout.CENTER);
            add(addOrDeletePanel, BorderLayout.SOUTH);
            locomotivesListPanel.add(selectedProductDetailsPanel, BorderLayout.CENTER);
            
            
            
            
            
            
            //Create Locomotive List Panel Items
            JLabel locomotiveListLabel = new JLabel("Locomotive Product List:");
            JList<String> locomotiveList = new JList<String>(locomotiveVector);
            JList<String> productInformationList = new JList<String>(productInformationVector);
            locomotiveList.setPreferredSize(new Dimension(200, 200));
            locomotiveList.setSelectedIndex(0);
            productInformationList.setSelectedIndex(0);
            selectedValueInformation = productInformationList.getSelectedValue();
            selectedIndex = 0;
            selectedValue = locomotiveList.getSelectedValue();
            JLabel productInfo = new JLabel("Selected Locomotive Product Info:");
            productInfo.setFont(new Font("Dialog", Font.BOLD, 22));
            selectedProductDetailsPanel.add(productInfo);
            JLabel productName = new JLabel("Product Name: ");
            productName.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productName);
            JLabel productCode = new JLabel("Product Code: ");
            productCode.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productCode);
            JLabel controlType = new JLabel("Product Control Type: ");
            controlType.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(controlType);
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
                    	String[] detailToEditChoices = {"Name", "Control Type", "ERA Code", "Cost", "Stock", "Gauge", "Scale"};
        				String detailToEdit = (String) JOptionPane.showInputDialog(null, "Select Detail To Edit", "Edit Existing Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, detailToEditChoices, detailToEditChoices[0]);
        				try {
        					EditLocomotives editLocomotives;
        					String selectedProduct = locomotiveList.getSelectedValue();
        					selectedValueProductCode = selectedValue.substring(0, selectedValue.indexOf(","));
        					if (detailToEdit != null) {
                				switch(detailToEdit) {
                				case "Name":
                					String newProductName = JOptionPane.showInputDialog(null, "Enter New Name For Product", "Edit Existing Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
                					if (newProductName == null) {
                						break;
                					}
                					PreparedStatement editProductNamestmt = connection.prepareStatement("UPDATE Products SET product_name = '" + newProductName +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductNamestmt.executeUpdate();
                					dispose();
                					editLocomotives = new EditLocomotives();
                					editLocomotives.setLocationRelativeTo(null);
                					break;
                				case "Control Type":
                					String[] controlTypeChoices = {"ANALOGUE", "DCC-READY", "DCC-FITTED", "DCC-SOUND"};
                					String newControlType = (String) JOptionPane.showInputDialog(null, "Select New Control Type", "Edit Existing Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, controlTypeChoices, controlTypeChoices[0]);
                					if (newControlType == null) {
                						break;
                					}
                					PreparedStatement editProductControlTypestmt = connection.prepareStatement("UPDATE Locomotive SET control_type = '" + newControlType +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductControlTypestmt.executeUpdate();
                					dispose();
                					editLocomotives = new EditLocomotives();
                					editLocomotives.setLocationRelativeTo(null);
                					break;
                				case "ERA Code":
                					String newProductEraCode = JOptionPane.showInputDialog(null, "Enter New ERA Code For Product", "Edit Existing Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
                					if (newProductEraCode == null) {
                						break;
                					}
                					PreparedStatement editProductEraCodestmt = connection.prepareStatement("UPDATE Locomotive SET eraCode = '" + newProductEraCode +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductEraCodestmt.executeUpdate();
                					dispose();
                					editLocomotives = new EditLocomotives();
                					editLocomotives.setLocationRelativeTo(null);
                					break;
                				case "Cost":
                        			String newProductCost = JOptionPane.showInputDialog(null, "Enter New Cost For Product", "Edit Existing Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
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
                    					editLocomotives = new EditLocomotives();
                    					editLocomotives.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Stock":
                					String newProductStock = JOptionPane.showInputDialog(null, "Enter New Stock For Product", "Edit Existing Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
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
                    					editLocomotives = new EditLocomotives();
                    					editLocomotives.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Gauge":
                					String[] gaugeChoices = {"OO", "TT", "N"};
                					String newGauge = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, gaugeChoices, gaugeChoices[0]);
                					if (newGauge == null) {
                						break;
                					}
                					PreparedStatement editProductGaugestmt = connection.prepareStatement("UPDATE Products SET gauge = '" + newGauge +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductGaugestmt.executeUpdate();
                					dispose();
                					editLocomotives = new EditLocomotives();
                					editLocomotives.setLocationRelativeTo(null);
                					break;
                				case "Scale":
                					String[] scaleChoices = {"1/76", "1/120", "1/148"};
                					String newScale = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
                					if (newScale == null) {
                						break;
                					}
                					PreparedStatement editProductScalestmt = connection.prepareStatement("UPDATE Products SET scale = '" + newScale +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductScalestmt.executeUpdate();
                					dispose();
                					editLocomotives = new EditLocomotives();
                					editLocomotives.setLocationRelativeTo(null);
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
                    String newProductName = JOptionPane.showInputDialog(null, "Enter Product Name", "Add New Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
                    if (newProductName != null && newProductName != "") {
                    	String newProductManufacturer = JOptionPane.showInputDialog(null, "Enter Manufacturer Name", "Add New Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
                    	if (newProductManufacturer != null && newProductManufacturer != "") {
                    		String newProductRetailPrice = JOptionPane.showInputDialog(null, "Enter Retail Price (e.g: 100, 1.50, 2)", "Add New Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
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
                        			String newProductStock = JOptionPane.showInputDialog(null, "Enter Current Stock", "Add New Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
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
                            				String newProductGauge = (String) JOptionPane.showInputDialog(null, "Select Gauge", "Add New Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, gaugeChoices, gaugeChoices[0]);
                            				if (newProductGauge != null) {
                            					String[] scaleChoices = {"1/76", "1/120", "1/148"};
                                				String newProductScale = (String) JOptionPane.showInputDialog(null, "Select Scale", "Add New Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
                                				if (newProductScale != null) {
                                					String[] controlTypeChoices = {"ANALOGUE", "DCC-READY", "DCC-FITTED", "DCC-SOUND"};
                                					String newProductControlType = (String) JOptionPane.showInputDialog(null, "Select Control Type", "Add New Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, controlTypeChoices, controlTypeChoices[0]);
                                					if (newProductControlType != null) {
                                						String newProductEraCode = JOptionPane.showInputDialog(null, "Enter Era Code", "Add New Locomotive Product", JOptionPane.INFORMATION_MESSAGE);
                                						if (newProductEraCode != null && newProductEraCode != "") {
                                        					try {
                                        						String newProductCode = null;
                                        						PreparedStatement getProductCodesstmt = connection.prepareStatement("SELECT product_code FROM Products WHERE product_code LIKE 'L%'");
                                        						ResultSet productCodeResultSet = getProductCodesstmt.executeQuery();
                                        						List<String> existingProductCodes = new ArrayList<>();
                                        						while (productCodeResultSet.next()) {
                                        							existingProductCodes.add(productCodeResultSet.getString("product_code").substring(1));
                                        						}
                                        						for(int i=1; i<10000; i++) {
                                        							if (existingProductCodes.contains(Integer.toString(i))) {
                                        								continue;
                                        							} else {
                                        								newProductCode = Integer.toString(i);
                                        								break;
                                        							}
                                        						}
                                        						
                                        						
                                        						
                                        						//CREATE NEW RECORD WITH GIVEN DETAILS (PRODUCT CODE NEEDS TO START WITH L)
                                        						PreparedStatement createNewProductstmt = connection.prepareStatement("INSERT INTO Products (product_code, product_name, manufacturer_name, retail_price, stock, gauge, scale) VALUES (?, ?, ?, ?, ?, ?, ?)");
                                        						createNewProductstmt.setString(1, "L" + newProductCode);
                                        						createNewProductstmt.setString(2, newProductName);
                                        						createNewProductstmt.setString(3, newProductManufacturer);
                                        						createNewProductstmt.setString(4, newProductRetailPrice);
                                        						createNewProductstmt.setString(5, newProductStock);
                                        						createNewProductstmt.setString(6, newProductGauge);
                                        						createNewProductstmt.setString(7, newProductScale);
        														int rowsUpdated = createNewProductstmt.executeUpdate();
        							                			if (rowsUpdated == 1) {
        							                				PreparedStatement createNewLocomotivestmt = connection.prepareStatement("INSERT INTO Locomotive (product_code, control_type, eraCode) VALUES (?, ?, ?)");
        							                				createNewLocomotivestmt.setString(1, "L" + newProductCode);
        							                				createNewLocomotivestmt.setString(2, newProductControlType);
        							                				createNewLocomotivestmt.setString(3, newProductEraCode);
        							                				createNewLocomotivestmt.executeUpdate();
        							    			                System.out.println("Product successfully added.");
        							    			                locomotiveVector.clear();
        							    			                productInformationVector.clear();
        							    			                locomotiveList.clearSelection();
        							    			                productInformationList.clearSelection();
        							    			                getAllLocomotivestmt = connection.createStatement();
        							    			                //Get All Locomotive Products
        							    			                locomotiveListResultSet = getAllLocomotivestmt.executeQuery("SELECT * FROM Products INNER JOIN Locomotive ON Products.product_code = Locomotive.product_code");
        							    			                
        							    			                while (locomotiveListResultSet.next()) {
        							    			                    String row = locomotiveListResultSet.getString("product_code") + ", " + locomotiveListResultSet.getString("product_name") + ", " + locomotiveListResultSet.getString("eraCode");
        							    			                    String productRow = locomotiveListResultSet.getString("product_name") + ", £" + locomotiveListResultSet.getString("retail_price") + ", " + locomotiveListResultSet.getString("stock") + ", " + locomotiveListResultSet.getString("gauge") + ", " + locomotiveListResultSet.getString("scale");
        							    			                    productInformationVector.add(productRow);
        							    			                    locomotiveVector.add(row);
        							    			                }
        							    			                
        							    			                
        							    			                
        							    			                JList<String> locomotiveList = new JList<String>(locomotiveVector);
        							    			                JList<String> productInformationList = new JList<String>(productInformationVector);
        							    			                locomotiveList.setSelectedIndex(0);
        							    			                selectedValue = locomotiveList.getSelectedValue();
        							    	                        productInformationList.setSelectedIndex(0);
        							    	                        selectedValueInformation = productInformationList.getSelectedValue();
        							    	                        
        							    	                        
        							    	                        
        							    			                if (selectedValue != null) {
        							    			                    int firstCommaIndex = selectedValue.indexOf(",");
        							    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
        							    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
        							    			                    selectedValueControlType = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
        							    			                    selectedValueEraCode = selectedValue.substring(secondCommaIndex + 2, selectedValue.length());
        							    			                    
        							    			                    int firstCommaIndex3 = selectedValueInformation.indexOf(",");
        							    			                    selectedValueName = selectedValueInformation.substring(0, firstCommaIndex3);
        							    			                    int secondCommaIndex3 = selectedValueInformation.indexOf(",", firstCommaIndex3 + 1);
        							    			                    selectedValueCost = selectedValueInformation.substring(firstCommaIndex3 + 3, secondCommaIndex3);
        							    			                    int thirdCommaIndex3 = selectedValueInformation.indexOf(", ", secondCommaIndex3 + 1);
        							    			                    selectedValueStock = selectedValueInformation.substring(secondCommaIndex3 + 2, thirdCommaIndex3);
        							    			                    int fourthCommaIndex3 = selectedValueInformation.indexOf(", ", thirdCommaIndex3 + 1);
        							    			                    selectedValueGauge = selectedValueInformation.substring(thirdCommaIndex3 + 2, fourthCommaIndex3);
        							    			                    selectedValueScale = selectedValueInformation.substring(fourthCommaIndex3 + 2, selectedValueInformation.length());
        							    			                    
        							    			                    
        							    			                    
        							    			                    
        							    			                    //DISPLAYING SELECTED LOCOMOTIVE DETAILS
        							    			                    productName.setText("Product Name: " + selectedValueName);
        							    			                    productCode.setText("Product Code: " + selectedValueProductCode);
        							    			                    controlType.setText("Control Type: " + selectedValueControlType);
        							    			                    eraCode.setText("Era Code: " + selectedValueEraCode);
        							    			                    productCost.setText("Product Cost: £" + selectedValueCost);
        							    			                    productStock.setText("Product Stock: " + selectedValueStock);
        							    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
        							    			                    productScale.setText("Product Scale: " + selectedValueScale);
        							    				                
        							    				                
        							    				                repaint();
        							    			                } else {
        							    			                	productCode.setText("Product Code: ");
        							    			                	controlType.setText("Control Type: ");
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
							PreparedStatement deleteProductstmt = connection.prepareStatement("DELETE FROM Locomotive WHERE product_code='" + selectedValueProductCode + "'");
							PreparedStatement deleteLocomotivestmt = connection.prepareStatement("DELETE FROM Products WHERE product_code='" + selectedValueProductCode + "'");
                			int rowsUpdated = deleteProductstmt.executeUpdate();
                			deleteLocomotivestmt.executeUpdate();
                			if (rowsUpdated == 1) {
    			                System.out.println("Product successfully deleted.");
    			                locomotiveVector.clear();
    			                productInformationVector.clear();
    			                locomotiveList.clearSelection();
    			                productInformationList.clearSelection();
    			                getAllLocomotivestmt = connection.createStatement();
    			                //Get All Locomotive Products
    			                locomotiveListResultSet = getAllLocomotivestmt.executeQuery("SELECT * FROM Products INNER JOIN Locomotive ON Products.product_code = Locomotive.product_code");
    			                
    			                while (locomotiveListResultSet.next()) {
    			                    String row = locomotiveListResultSet.getString("product_code") + ", " + locomotiveListResultSet.getString("product_name") + ", " + locomotiveListResultSet.getString("eraCode");
    			                    String productRow = locomotiveListResultSet.getString("product_name") + ", £" + locomotiveListResultSet.getString("retail_price") + ", " + locomotiveListResultSet.getString("stock") + ", " + locomotiveListResultSet.getString("gauge") + ", " + locomotiveListResultSet.getString("scale");
    			                    productInformationVector.add(productRow);
    			                    locomotiveVector.add(row);
    			                }
    			                JList<String> locomotiveList = new JList<String>(locomotiveVector);
    			                locomotiveList.setSelectedIndex(0);
    			                selectedValue = locomotiveList.getSelectedValue();
    	                        productInformationList.setSelectedIndex(0);
    	                        selectedValueInformation = productInformationList.getSelectedValue();
    			                if (selectedValue != null) {
    			                    int firstCommaIndex = selectedValue.indexOf(",");
    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
    			                    selectedValueControlType = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
    			                    selectedValueEraCode = selectedValue.substring(secondCommaIndex + 2, selectedValue.length());
    			                    
    			                    int firstCommaIndex3 = selectedValueInformation.indexOf(",");
    			                    selectedValueName = selectedValueInformation.substring(0, firstCommaIndex3);
    			                    int secondCommaIndex3 = selectedValueInformation.indexOf(",", firstCommaIndex3 + 1);
    			                    selectedValueCost = selectedValueInformation.substring(firstCommaIndex3 + 3, secondCommaIndex3);
    			                    int thirdCommaIndex3 = selectedValueInformation.indexOf(", ", secondCommaIndex3 + 1);
    			                    selectedValueStock = selectedValueInformation.substring(secondCommaIndex3 + 2, thirdCommaIndex3);
    			                    int fourthCommaIndex3 = selectedValueInformation.indexOf(", ", thirdCommaIndex3 + 1);
    			                    selectedValueGauge = selectedValueInformation.substring(thirdCommaIndex3 + 2, fourthCommaIndex3);
    			                    selectedValueScale = selectedValueInformation.substring(fourthCommaIndex3 + 2, selectedValueInformation.length());
    			                    
    			                    
    			                    
    			                    
    			                    //DISPLAYING SELECTED LOCOMOTIVE DETAILS
    			                    productName.setText("Product Name: " + selectedValueName);
    			                    productCode.setText("Product Code: " + selectedValueProductCode);
    			                    controlType.setText("Control Type: " + selectedValueControlType);
    			                    eraCode.setText("Era Code: " + selectedValueEraCode);
    			                    productCost.setText("Product Cost: " + selectedValueCost);
    			                    productStock.setText("Product Stock: " + selectedValueStock);
    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
    			                    productScale.setText("Product Scale: " + selectedValueScale);
    				                
    				                
    				                repaint();
    			                } else {
    			                	productCode.setText("Product Code: ");
    			                	controlType.setText("Control Type: ");
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
                selectedValueControlType = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
                selectedValueEraCode = selectedValue.substring(secondCommaIndex + 2, selectedValue.length());
                
                int firstCommaIndex3 = selectedValueInformation.indexOf(",");
                selectedValueName = selectedValueInformation.substring(0, firstCommaIndex3);
                int secondCommaIndex3 = selectedValueInformation.indexOf(",", firstCommaIndex3 + 1);
                selectedValueCost = selectedValueInformation.substring(firstCommaIndex3 + 3, secondCommaIndex3);
                int thirdCommaIndex3 = selectedValueInformation.indexOf(", ", secondCommaIndex3 + 1);
                selectedValueStock = selectedValueInformation.substring(secondCommaIndex3 + 2, thirdCommaIndex3);
                int fourthCommaIndex3 = selectedValueInformation.indexOf(", ", thirdCommaIndex3 + 1);
                selectedValueGauge = selectedValueInformation.substring(thirdCommaIndex3 + 2, fourthCommaIndex3);
                selectedValueScale = selectedValueInformation.substring(fourthCommaIndex3 + 2, selectedValueInformation.length());
                
                //DISPLAYING SELECTED LOCOMOTIVE DETAILS
                productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: " + selectedValueProductCode);
                controlType.setText("Control Type: " + selectedValueControlType);
                eraCode.setText("Era Code: " + selectedValueEraCode);
                productCost.setText("Product Cost: " + selectedValueCost);
                productStock.setText("Product Stock: " + selectedValueStock);
                productGauge.setText("Product Gauge: " + selectedValueGauge);
                productScale.setText("Product Scale: " + selectedValueScale);
            } else {
            	productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: ");
                controlType.setText("Control Type: ");
                eraCode.setText("Era Code: ");
                productCost.setText("Product Cost: ");
                productGauge.setText("Product Gauge: ");
                productScale.setText("Product Scale: ");
            }
            

            
            
            locomotiveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            locomotiveList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedValue = locomotiveList.getSelectedValue();
                        selectedIndex = locomotiveList.getSelectedIndex();
                        productInformationList.setSelectedIndex(selectedIndex);
                        selectedValueInformation = productInformationList.getSelectedValue();
                        if (selectedValue != null) {
                            int firstCommaIndex2 = selectedValue.indexOf(",");
                            selectedValueProductCode = selectedValue.substring(0, firstCommaIndex2);
                            int secondCommaIndex2 = selectedValue.indexOf(",", firstCommaIndex2 + 1);
                            selectedValueControlType = selectedValue.substring(firstCommaIndex2 + 2, secondCommaIndex2);
                            selectedValueEraCode = selectedValue.substring(secondCommaIndex2 + 2, selectedValue.length());
                             
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
                            controlType.setText("Control Type: " + selectedValueControlType);
                            eraCode.setText("Era Code: " + selectedValueEraCode);
                            productCost.setText("Product Cost: " + selectedValueCost);
                            productStock.setText("Product Stock: " + selectedValueStock);
                            productGauge.setText("Product Gauge: " + selectedValueGauge);
                            productScale.setText("Product Scale: " + selectedValueScale);
   
                            
                        }
                    }
                }
            });
            


            
            //Add List to Scrollable Pane
            JScrollPane staffScrollableList = new JScrollPane(locomotiveList);
            //Add Items to Demote User Panel 
            locomotivesListPanel.add(locomotiveListLabel, BorderLayout.NORTH);
            locomotivesListPanel.add(staffScrollableList, BorderLayout.WEST);
            //Add Items to AddOrDelete Product Panel
            addOrDeletePanel.add(addProductButton);
            addOrDeletePanel.add(deleteProductButton);
            
            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    
    