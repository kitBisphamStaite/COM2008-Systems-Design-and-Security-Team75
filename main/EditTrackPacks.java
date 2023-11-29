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

public class EditTrackPacks extends JFrame {	
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
    Connection connection;
    
    //SQL
    Statement getAllTrackPackstmt;
    ResultSet TrainSetListResultSet;
    Vector<String> productInformationVector = new Vector<String>();
    //SelectedValue from List
	String selectedValue;
	int selectedIndex;
	JList<String> trackPackList = new JList<String>();	
	//Variables in every category
	String selectedValueInformation;
	String selectedValueProductCode;
	String selectedValueCost;
	String selectedValueName;
	String selectedValueStock;
	String selectedValueGauge;
	String selectedValueScale;
	
    public EditTrackPacks() {
        //GET LIST OF TRACKS
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            getAllTrackPackstmt = connection.createStatement();
            //Get All Track Products
            TrainSetListResultSet = getAllTrackPackstmt.executeQuery("SELECT * FROM Products WHERE product_code LIKE 'P%'");
            
            while (TrainSetListResultSet.next()) {
                String productRow = TrainSetListResultSet.getString("product_code") + ", " + TrainSetListResultSet.getString("product_name") + ", £" + TrainSetListResultSet.getString("retail_price") + ", " + TrainSetListResultSet.getString("stock") + ", " + TrainSetListResultSet.getString("gauge") + ", " + TrainSetListResultSet.getString("scale");
                productInformationVector.add(productRow);
            }
  
            //Set Up Frame
            setTitle("Edit Track Packs");
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
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Track Pack Products");
            trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
            //Add Items To Header Panel
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
            
            //Create Panels
            JPanel trackPackListPanel = new JPanel(new BorderLayout());
            JPanel addOrDeletePanel = new JPanel(new GridLayout(0,2));
            JPanel selectedProductDetailsPanel = new JPanel(new GridLayout(15,0,0,0));
            //Add Panels To Frame
            add(headerPanel, BorderLayout.NORTH);
            add(trackPackListPanel, BorderLayout.CENTER);
            add(addOrDeletePanel, BorderLayout.SOUTH);
            trackPackListPanel.add(selectedProductDetailsPanel, BorderLayout.CENTER);
            
            
            
            
            
            
            //Create Track List Panel Items
            JLabel trackPackListLabel = new JLabel("Track Pack Product List:");
            JList<String> productInformationList = new JList<String>(productInformationVector);
            System.out.println(productInformationVector);
            productInformationList.setPreferredSize(new Dimension(200, 200));
            productInformationList.setSelectedIndex(0);
            selectedValueInformation = productInformationList.getSelectedValue();
            selectedIndex = 0;
            JLabel productInfo = new JLabel("Selected Track Pack Product Info:");
            productInfo.setFont(new Font("Dialog", Font.BOLD, 22));
            selectedProductDetailsPanel.add(productInfo);
            JLabel productName = new JLabel("Product Name: ");
            productName.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productName);
            JLabel productCode = new JLabel("Product Code: ");
            productCode.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productCode);
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
                    	String[] detailToEditChoices = {"Name", "Cost", "Stock", "Gauge", "Scale"};
        				String detailToEdit = (String) JOptionPane.showInputDialog(null, "Select Detail To Edit", "Edit Existing Train Set Product", JOptionPane.QUESTION_MESSAGE, null, detailToEditChoices, detailToEditChoices[0]);
        				try {
        					EditTrackPacks editTrackPacks;
        					String selectedProduct = productInformationList.getSelectedValue();
        					selectedValueProductCode = selectedValue.substring(0, selectedValue.indexOf(","));
        					if (detailToEdit != null) {
                				switch(detailToEdit) {
                				case "Name":
                					String newProductName = JOptionPane.showInputDialog(null, "Enter New Name For Product", "Edit Existing Track Pack Product", JOptionPane.INFORMATION_MESSAGE);
                					if (newProductName == null) {
                						break;
                					}
                					PreparedStatement editProductNamestmt = connection.prepareStatement("UPDATE Products SET product_name = '" + newProductName +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductNamestmt.executeUpdate();
                					dispose();
                					editTrackPacks = new EditTrackPacks();
                					editTrackPacks.setLocationRelativeTo(null);
                					break;
                				case "Cost":
                        			String newProductCost = JOptionPane.showInputDialog(null, "Enter New Cost For Product", "Edit Existing Track Pack Product", JOptionPane.INFORMATION_MESSAGE);
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
                    					editTrackPacks = new EditTrackPacks();
                    					editTrackPacks.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Stock":
                					String newProductStock = JOptionPane.showInputDialog(null, "Enter New Stock For Product", "Edit Existing Track Pack Product", JOptionPane.INFORMATION_MESSAGE);
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
                    					editTrackPacks = new EditTrackPacks();
                    					editTrackPacks.setLocationRelativeTo(null);
                					break;
                        			}
                				case "Gauge":
                					String[] gaugeChoices = {"OO", "TT", "N"};
                					String newGauge = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Track Pack Product", JOptionPane.QUESTION_MESSAGE, null, gaugeChoices, gaugeChoices[0]);
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
                					editTrackPacks = new EditTrackPacks();
                					editTrackPacks.setLocationRelativeTo(null);
                					break;
                				case "Scale":
                					String[] scaleChoices = {"1/76", "1/120", "1/148"};
                					String newScale = (String) JOptionPane.showInputDialog(null, "Select New Gauge", "Edit Existing Track Pack Product", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
                					if (newScale == null) {
                						break;
                					}
                					switch (newScale) {
                					case "1/76":
                						newScale = "1";
                						break;
                					case "1/120":
                						newScale = "2";
                						break;
                					case "1/148":
                						newScale = "3";
                						break;
                					}
                					PreparedStatement editProductScalestmt = connection.prepareStatement("UPDATE Products SET scale = '" + newScale +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductScalestmt.executeUpdate();
                					dispose();
                					editTrackPacks = new EditTrackPacks();
                					editTrackPacks.setLocationRelativeTo(null);
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
                    String newProductName = JOptionPane.showInputDialog(null, "Enter Product Name", "Add New Track Pack Product", JOptionPane.INFORMATION_MESSAGE);
                    if (newProductName != null && newProductName != "") {
                    	String newProductManufacturer = JOptionPane.showInputDialog(null, "Enter Manufacturer Name", "Add New Track Pack Product", JOptionPane.INFORMATION_MESSAGE);
                    	if (newProductManufacturer != null && newProductManufacturer != "") {
                    		String newProductRetailPrice = JOptionPane.showInputDialog(null, "Enter Retail Price (e.g: 100, 1.50, 2)", "Add New Track Pack Product", JOptionPane.INFORMATION_MESSAGE);
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
                        			String newProductStock = JOptionPane.showInputDialog(null, "Enter Current Stock", "Add New Track Pack Product", JOptionPane.INFORMATION_MESSAGE);
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
                                				String newProductScale = (String) JOptionPane.showInputDialog(null, "Select Scale", "Add New Locomotive Product", JOptionPane.QUESTION_MESSAGE, null, scaleChoices, scaleChoices[0]);
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
                                					try {
                                						String newProductCode = null;
                                        				PreparedStatement getProductCodesstmt = connection.prepareStatement("SELECT product_code FROM Products WHERE product_code LIKE 'P%'");
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
                                        						
                                        						
                                        						
                                        						//CREATE NEW RECORD WITH GIVEN DETAILS (PRODUCT CODE NEEDS TO START WITH L)
                                        						PreparedStatement createNewProductstmt = connection.prepareStatement("INSERT INTO Products (product_code, product_name, manufacturer_name, retail_price, stock, gauge, scale) VALUES (?, ?, ?, ?, ?, ?, ?)");
                                        						createNewProductstmt.setString(1, "P" + newProductCode);
                                        						createNewProductstmt.setString(2, newProductName);
                                        						createNewProductstmt.setString(3, newProductManufacturer);
                                        						createNewProductstmt.setString(4, newProductRetailPrice);
                                        						createNewProductstmt.setString(5, newProductStock);
                                        						createNewProductstmt.setString(6, newProductGauge);
                                        						createNewProductstmt.setString(7, newProductScale);
        														int rowsUpdated = createNewProductstmt.executeUpdate();
        							                			if (rowsUpdated == 1) {
        							                				PreparedStatement createNewTrackPackstmt = connection.prepareStatement("INSERT INTO Track_Pack (product_code) VALUES (?)");
        							                				createNewTrackPackstmt.setString(1, "P" + newProductCode);
        							                				createNewTrackPackstmt.executeUpdate();
        							    			                System.out.println("Product successfully added.");
        							    			                productInformationVector.clear();
        							    			                productInformationList.clearSelection();
        							    			                getAllTrackPackstmt = connection.createStatement();
        							    			                //Get All Locomotive Products
        							    			                TrainSetListResultSet = getAllTrackPackstmt.executeQuery("SELECT * FROM Products WHERE product_code LIKE 'P%'");
        							    			                while (TrainSetListResultSet.next()) {
        							    			                    String productRow = TrainSetListResultSet.getString("product_code") + ", " + TrainSetListResultSet.getString("product_name") + ", £" + TrainSetListResultSet.getString("retail_price") + ", " + TrainSetListResultSet.getString("stock") + ", " + TrainSetListResultSet.getString("gauge") + ", " + TrainSetListResultSet.getString("scale");
        							    			                    productInformationVector.add(productRow);
        							    			                }
        							    			                
        							    			                
        							    			                
        							    			                JList<String> trackPackList = new JList<String>(productInformationVector);
        							    			                trackPackList.setSelectedIndex(0);
        							    			                selectedValue = trackPackList.getSelectedValue();
        							    	                        
        							    	                        
        							    	                        
        							    			                if (selectedValue != null) {
        							    			                    int firstCommaIndex = selectedValue.indexOf(",");
        							    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
        							    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
        							    			                    selectedValueName = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
        							    			                    int thirdCommaIndex = selectedValue.indexOf(",", secondCommaIndex + 1);
        							    			                    selectedValueCost = selectedValue.substring(secondCommaIndex + 3, thirdCommaIndex);
        							    			                    int fourthCommaIndex = selectedValue.indexOf(",", thirdCommaIndex + 1);
        							    			                    selectedValueStock = selectedValue.substring(thirdCommaIndex + 1, fourthCommaIndex);
        							    			                    int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
        							    			                    selectedValueGauge = selectedValue.substring(fourthCommaIndex +1, fifthCommaIndex);
        							    			                    selectedValueStock = selectedValue.substring(fifthCommaIndex + 1, selectedValue.length());
        							    			                    
        							    			                    
        							    		
        							    			                    
        							    			                    
        							    			                    
        							    			                    
        							    			                    //DISPLAYING SELECTED LOCOMOTIVE DETAILS
        							    			                    productName.setText("Product Name: " + selectedValueName);
        							    			                    productCode.setText("Product Code: " + selectedValueProductCode);
        							    			                    productCost.setText("Product Cost: £" + selectedValueCost);
        							    			                    productStock.setText("Product Stock: " + selectedValueStock);
        							    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
        							    			                    productScale.setText("Product Scale: " + selectedValueScale);
        							    				                
        							    				                
        							    				                repaint();
        							    			                } else {
        							    			                	productName.setText("Product Name: ");
        							    			                	productCode.setText("Product Code: ");
        							    			                    productCost.setText("Product Cost: £");
        							    			                    productGauge.setText("Product Gauge: ");
        							    			                    productScale.setText("Product Scale: ");
        							    			                	repaint();
        							    			                }
        							    			                JOptionPane.showMessageDialog(null, "Successfully added new product.");
        							            					dispose();
        							            					EditTrackPacks editTrackPacks= new EditTrackPacks();
        							            					editTrackPacks.setLocationRelativeTo(null);
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
            });
            
            
            
            
            
            
            
            
            
            JButton deleteProductButton = new JButton("Delete Selected Product");
            deleteProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (selectedValueProductCode != null) {
                		try {
							PreparedStatement deleteTrackPackstmt = connection.prepareStatement("DELETE FROM Track_Pack WHERE product_code='" + selectedValueProductCode + "'");
							PreparedStatement deleteProductstmt = connection.prepareStatement("DELETE FROM Products WHERE product_code='" + selectedValueProductCode + "'");
                			int rowsUpdated = deleteTrackPackstmt.executeUpdate();
                			deleteProductstmt.executeUpdate();
                			if (rowsUpdated == 1) {
    			                System.out.println("Product successfully deleted.");
    			                productInformationVector.clear();
    			                trackPackList.clearSelection();
    			                getAllTrackPackstmt = connection.createStatement();
    			                //Get All Track Products
    			                TrainSetListResultSet = getAllTrackPackstmt.executeQuery("SELECT * FROM Products WHERE product_code LIKE 'P%");
    			                
    			                while (TrainSetListResultSet.next()) {
    			                    String productRow = TrainSetListResultSet.getString("product_code") + ", " + TrainSetListResultSet.getString("product_name") + ", £" + TrainSetListResultSet.getString("retail_price") + ", " + TrainSetListResultSet.getString("stock") + ", " + TrainSetListResultSet.getString("gauge") + ", " + TrainSetListResultSet.getString("scale");
    			                    productInformationVector.add(productRow);
    			                }
    			                JList<String> trackPackList = new JList<String>(productInformationVector);
    			                trackPackList.setSelectedIndex(0);
    			                selectedValue = trackPackList.getSelectedValue();
    			                if (selectedValue != null) {
    			                    int firstCommaIndex = selectedValue.indexOf(",");
    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
    			                    selectedValueName = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
    			                    int thirdCommaIndex = selectedValue.indexOf(",", secondCommaIndex + 1);
    			                    selectedValueCost = selectedValue.substring(secondCommaIndex + 3, thirdCommaIndex);
    			                    int fourthCommaIndex = selectedValue.indexOf(",", thirdCommaIndex + 1);
    			                    selectedValueStock = selectedValue.substring(thirdCommaIndex + 1, fourthCommaIndex);
    			                    int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
    			                    selectedValueGauge = selectedValue.substring(fourthCommaIndex +1, fifthCommaIndex);
    			                    selectedValueStock = selectedValue.substring(fifthCommaIndex + 1, selectedValue.length());
    			                    
    			                    
    		
    			                    
    			                    
    			                    
    			                    
    			                    //DISPLAYING SELECTED LOCOMOTIVE DETAILS
    			                    productName.setText("Product Name: " + selectedValueName);
    			                    productCode.setText("Product Code: " + selectedValueProductCode);
    			                    productCost.setText("Product Cost: £" + selectedValueCost);
    			                    productStock.setText("Product Stock: " + selectedValueStock);
    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
    			                    productScale.setText("Product Scale: " + selectedValueScale);
    				                
    				                
    				                
    				                
    				                repaint();
    			                } else {
    			                	productName.setText("Product Name: ");
    			                	productCode.setText("Product Code: ");
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
                selectedValueStock = selectedValue.substring(thirdCommaIndex + 1, fourthCommaIndex);
                int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
                selectedValueGauge = selectedValue.substring(fourthCommaIndex +1, fifthCommaIndex);
                selectedValueStock = selectedValue.substring(fifthCommaIndex + 1, selectedValue.length());
                //DISPLAYING SELECTED LOCOMOTIVE DETAILS
                productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: " + selectedValueProductCode);
                productCost.setText("Product Cost: £" + selectedValueCost);
                productStock.setText("Product Stock: " + selectedValueStock);
                productGauge.setText("Product Gauge: " + selectedValueGauge);
                productScale.setText("Product Scale: " + selectedValueScale);
                
                
            } else {
            	productName.setText("Product Name: ");
                productCode.setText("Product Code: ");
                productCost.setText("Product Cost: ");
                productGauge.setText("Product Gauge: ");
                productScale.setText("Product Scale: ");
            }
            

            
            
            productInformationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            productInformationList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                    	System.out.println("TEST!");
                        selectedValue = productInformationList.getSelectedValue();
                        selectedIndex = productInformationList.getSelectedIndex();
                        productInformationList.setSelectedIndex(selectedIndex);
                        selectedValueInformation = productInformationList.getSelectedValue();
                        if (selectedValue != null) {
                            int firstCommaIndex = selectedValue.indexOf(",");
                            selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
                            int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
                            selectedValueName = selectedValue.substring(firstCommaIndex + 2, secondCommaIndex);
                            int thirdCommaIndex = selectedValue.indexOf(",", secondCommaIndex + 1);
                            selectedValueCost = selectedValue.substring(secondCommaIndex + 3, thirdCommaIndex);
                            int fourthCommaIndex = selectedValue.indexOf(",", thirdCommaIndex + 1);
                            selectedValueStock = selectedValue.substring(thirdCommaIndex + 1, fourthCommaIndex);
                            int fifthCommaIndex = selectedValue.indexOf(",", fourthCommaIndex + 1);
                            selectedValueGauge = selectedValue.substring(fourthCommaIndex +1, fifthCommaIndex);
                            selectedValueStock = selectedValue.substring(fifthCommaIndex + 1, selectedValue.length());
                            //DISPLAYING SELECTED LOCOMOTIVE DETAILS
                            productName.setText("Product Name: " + selectedValueName);
                            productCode.setText("Product Code: " + selectedValueProductCode);
                            productCost.setText("Product Cost: £" + selectedValueCost);
                            productStock.setText("Product Stock: " + selectedValueStock);
                            productGauge.setText("Product Gauge: " + selectedValueGauge);
                            productScale.setText("Product Scale: " + selectedValueScale);
                        }
                    }
                }
            });
            

         
            
            //Add List to Scrollable Pane
            JScrollPane trackPackScrollableList = new JScrollPane(productInformationList);
            //Add Items to Demote User Panel 
            trackPackListPanel.add(trackPackListLabel, BorderLayout.NORTH);
            trackPackListPanel.add(trackPackScrollableList, BorderLayout.WEST);
            //Add Items to AddOrDelete Product Panel
            addOrDeletePanel.add(addProductButton);
            addOrDeletePanel.add(deleteProductButton);
            
            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    
    