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

public class EditTrainSets extends JFrame {	
	//Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";
    Connection connection;
    //SQL
    Statement getAllTrainSetstmt;
    ResultSet TrainSetListResultSet;
    Vector<String> TrainSetVector = new Vector<String>();
    Vector<String> productInformationVector = new Vector<String>();
    //SelectedValue from List
	String selectedValue;
	int selectedIndex;
	String selectedValueProductCode;
	JList<String> trainSetList = new JList<String>();
	//New variables depending on category
	String selectedValueControllerProductCode;
	String selectedValueEraCode;
	String selectedValueInformation;
	
	//Variables in every category
	String selectedValueCost;
	String selectedValueName;
	String selectedValueStock;
	String selectedValueGauge;
	String selectedValueScale;
	
    public EditTrainSets() {
        //GET LIST OF TRACKS
        try {
            connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
            getAllTrainSetstmt = connection.createStatement();
            //Get All Track Products
            TrainSetListResultSet = getAllTrainSetstmt.executeQuery("SELECT * FROM Products INNER JOIN Train_Set ON Products.product_code = Train_Set.product_code");
            
            while (TrainSetListResultSet.next()) {
                String row = TrainSetListResultSet.getString("product_code") + ", " + TrainSetListResultSet.getString("controller_product_code") + ", " + TrainSetListResultSet.getString("era_code");
                String productRow = TrainSetListResultSet.getString("product_name") + ", £" + TrainSetListResultSet.getString("retail_price") + ", " + TrainSetListResultSet.getString("stock") + ", " + TrainSetListResultSet.getString("gauge") + ", " + TrainSetListResultSet.getString("scale");
                productInformationVector.add(productRow);
                TrainSetVector.add(row);
            }
  
            //Set Up Frame
            setTitle("Edit Train Sets");
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
            JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Train Set Products");
            trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
            //Add Items To Header Panel
            headerPanel.add(backButton, BorderLayout.WEST);
            headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
            
            //Create Panels
            JPanel trainSetListPanel = new JPanel(new BorderLayout());
            JPanel addOrDeletePanel = new JPanel(new GridLayout(0,2));
            JPanel selectedProductDetailsPanel = new JPanel(new GridLayout(15,0,0,0));
            //Add Panels To Frame
            add(headerPanel, BorderLayout.NORTH);
            add(trainSetListPanel, BorderLayout.CENTER);
            add(addOrDeletePanel, BorderLayout.SOUTH);
            trainSetListPanel.add(selectedProductDetailsPanel, BorderLayout.CENTER);
            
            
            
            
            
            
            //Create Track List Panel Items
            JLabel trainSetListLabel = new JLabel("Train Set Product List:");
            JList<String> trainSetList = new JList<String>(TrainSetVector);
            JList<String> productInformationList = new JList<String>(productInformationVector);
            trainSetList.setPreferredSize(new Dimension(200, 200));
            trainSetList.setSelectedIndex(0);
            productInformationList.setSelectedIndex(0);
            selectedValueInformation = productInformationList.getSelectedValue();
            selectedIndex = 0;
            selectedValue = trainSetList.getSelectedValue();
            JLabel productInfo = new JLabel("Selected Train Set Product Info:");
            productInfo.setFont(new Font("Dialog", Font.BOLD, 22));
            selectedProductDetailsPanel.add(productInfo);
            JLabel productName = new JLabel("Product Name: ");
            productName.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productName);
            JLabel productCode = new JLabel("Product Code: ");
            productCode.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(productCode);
            JLabel controllerProductCode = new JLabel("Product Controller Code: ");
            controllerProductCode.setFont(new Font("Dialog", Font.BOLD, 14));
            selectedProductDetailsPanel.add(controllerProductCode);
            JLabel eraCode = new JLabel("Product ERA Code: ");
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
                    	String[] detailToEditChoices = {"Name", "Cost", "Stock", "Gauge", "Scale", "ERA Code"};
        				String detailToEdit = (String) JOptionPane.showInputDialog(null, "Select Detail To Edit", "Edit Existing Train Set Product", JOptionPane.QUESTION_MESSAGE, null, detailToEditChoices, detailToEditChoices[0]);
        				try {
        					EditTrainSets editTrainSets;
        					String selectedProduct = trainSetList.getSelectedValue();
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
                					editTrainSets = new EditTrainSets();
                					editTrainSets.setLocationRelativeTo(null);
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
                    					editTrainSets = new EditTrainSets();
                    					editTrainSets.setLocationRelativeTo(null);
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
                    					editTrainSets = new EditTrainSets();
                    					editTrainSets.setLocationRelativeTo(null);
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
                					editTrainSets = new EditTrainSets();
                					editTrainSets.setLocationRelativeTo(null);
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
                					editTrainSets = new EditTrainSets();
                					editTrainSets.setLocationRelativeTo(null);
                					break;
                				case "ERA Code":
                					String newProductEraCode = JOptionPane.showInputDialog(null, "Enter New ERA Code For Product", "Edit Existing Train Set Product", JOptionPane.INFORMATION_MESSAGE);
                					if (newProductEraCode == null) {
                						break;
                					}
                					PreparedStatement editProductEraCodestmt = connection.prepareStatement("UPDATE Train_Set SET era_code = '" + newProductEraCode +"' WHERE product_code ='" + selectedValueProductCode + "'");
                					editProductEraCodestmt.executeUpdate();
                					dispose();
                					editTrainSets = new EditTrainSets();
                					editTrainSets.setLocationRelativeTo(null);
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
                    String newProductName = JOptionPane.showInputDialog(null, "Enter Product Name", "Add New Train Set Product", JOptionPane.INFORMATION_MESSAGE);
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
                        			String newProductStock = JOptionPane.showInputDialog(null, "Enter Current Stock", "Add New Train Set Product", JOptionPane.INFORMATION_MESSAGE);
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
                                					try {
                                					String newProductControllerCode = (String) JOptionPane.showInputDialog(null, "Select Controller Code", "Add New Train Set Product", JOptionPane.INFORMATION_MESSAGE);
                                					PreparedStatement getControllerCodesstmt = connection.prepareStatement("SELECT product_code FROM Products WHERE product_code LIKE 'C%'");
                                					ResultSet controllerCodesResultSet = getControllerCodesstmt.executeQuery();
                                					List<String> existingControllerCodes = new ArrayList<>();
                                					while (controllerCodesResultSet.next()) {
                                						existingControllerCodes.add(controllerCodesResultSet.getString("product_code"));
                                					}
                                					System.out.println(existingControllerCodes);
                                					if (existingControllerCodes.contains(newProductControllerCode)) {
                                    					if (newProductControllerCode != null) {
                                    						String newProductEraCode = JOptionPane.showInputDialog(null, "Enter Era Code", "Add New Train Set Product", JOptionPane.INFORMATION_MESSAGE);
                                    						if (newProductEraCode != null) {
                                            					try {
                                            						String newProductCode = null;
                                            						PreparedStatement getProductCodesstmt = connection.prepareStatement("SELECT product_code FROM Products WHERE product_code LIKE 'M%'");
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
                                            						
                                            						
                                            						
                                            						//CREATE NEW RECORD WITH GIVEN DETAILS (PRODUCT CODE NEEDS TO START WITH R)
                                            						PreparedStatement createNewProductstmt = connection.prepareStatement("INSERT INTO Products (product_code, product_name, manufacturer_name, retail_price, stock, gauge, scale) VALUES (?, ?, ?, ?, ?, ?, ?)");
                                            						createNewProductstmt.setString(1, "M" + newProductCode);
                                            						createNewProductstmt.setString(2, newProductName);
                                            						createNewProductstmt.setString(3, newProductManufacturer);
                                            						createNewProductstmt.setString(4, newProductRetailPrice);
                                            						createNewProductstmt.setString(5, newProductStock);
                                            						createNewProductstmt.setString(6, newProductGauge);
                                            						createNewProductstmt.setString(7, newProductScale);
            														int rowsUpdated = createNewProductstmt.executeUpdate();
            							                			if (rowsUpdated == 1) {
            							                				PreparedStatement createNewTrainSetstmt = connection.prepareStatement("INSERT INTO Train_Set (product_code, controller_product_code, era_code) VALUES (?, ?, ?)");
            							                				createNewTrainSetstmt.setString(1, "M" + newProductCode);
            							                				createNewTrainSetstmt.setString(2, newProductControllerCode);
            							                				createNewTrainSetstmt.setString(3, newProductEraCode);
            							                				createNewTrainSetstmt.executeUpdate();
            							    			                System.out.println("Product successfully added.");
            							    			                TrainSetVector.clear();
            							    			                productInformationVector.clear();
            							    			                trainSetList.clearSelection();
            							    			                productInformationList.clearSelection();
            							    			                getAllTrainSetstmt = connection.createStatement();
            							    			                //Get All Track Products
            							    			                TrainSetListResultSet = getAllTrainSetstmt.executeQuery("SELECT * FROM Products INNER JOIN Train_Set ON Products.product_code = Train_Set.product_code");
            							    			                while (TrainSetListResultSet.next()) {
            							    			                    String row = TrainSetListResultSet.getString("product_code") + ", " + TrainSetListResultSet.getString("controller_product_code") + ", " + TrainSetListResultSet.getString("era_code");
            							    			                    String productRow = TrainSetListResultSet.getString("product_name") + ", £" + TrainSetListResultSet.getString("retail_price") + ", " + TrainSetListResultSet.getString("stock") + ", " + TrainSetListResultSet.getString("gauge") + ", " + TrainSetListResultSet.getString("scale");
            							    			                    productInformationVector.add(productRow);
            							    			                    TrainSetVector.add(row);
            							    			                }
            							    			                
            							    			                
            							    			                
            							    			                JList<String> trainSetList = new JList<String>(TrainSetVector);
            							    			                JList<String> productInformationList = new JList<String>(productInformationVector);
            							    			                trainSetList.setSelectedIndex(0);
            							    			                selectedValue = trainSetList.getSelectedValue();
            							    	                        productInformationList.setSelectedIndex(0);
            							    	                        selectedValueInformation = productInformationList.getSelectedValue();
            							    	                        
            							    			                if (selectedValue != null) {
            							    			                    int firstCommaIndex = selectedValue.indexOf(",");
            							    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
            							    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
            							    			                    selectedValueControllerProductCode = selectedValue.substring(firstCommaIndex + 2,  secondCommaIndex);
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
            							    			                    
            							    			                    
            							    			                    
            							    			                    
            							    			                    //DISPLAYING SELECTED TRACK DETAILS
            							    			                    productName.setText("Product Name: " + selectedValueName);
            							    			                    productCode.setText("Product Code: " + selectedValueProductCode);
            							    			                    controllerProductCode.setText("Controller Code: " + selectedValueControllerProductCode);
            							    			                    productCost.setText("Product Cost: £" + selectedValueCost);
            							    			                    productStock.setText("Product Stock: " + selectedValueStock);
            							    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
            							    			                    productScale.setText("Product Scale: " + selectedValueScale);
            							    			                    JOptionPane.showMessageDialog(null, "Successfully added new product.");
            							    				                
            							    				                repaint();
            							    			                } else {
            							    			                	productCode.setText("Product Code: ");
            							    			                	controllerProductCode.setText("Controller Code: ");
            							    			                    productCost.setText("Product Cost: £");
            							    			                    productGauge.setText("Product Gauge: ");
            							    			                    productScale.setText("Product Scale: ");
            							    			                	repaint();
            							    			                }
            							            					dispose();
            							            					EditTrainSets editTrainSets = new EditTrainSets();
            							            					editTrainSets.setLocationRelativeTo(null);
            							                			}
            														
            														
            													} catch (SQLException e1) {
            														e1.printStackTrace();
            													}
                                    						}
                                						}
                                					} else {
                                						JOptionPane.showMessageDialog(null, "ERROR. Unable to find existing inputted controller code");
                                					}
                                					}
                                				catch(SQLException e2) {
                                					e2.printStackTrace();
                                				}
                                				}
                            				}
                            			}
                        			}
                    			}
                    		}
                    	}
                    }
                }     });
            
            
            
            
            
            
            
            
            
            JButton deleteProductButton = new JButton("Delete Selected Product");
            deleteProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	if (selectedValueProductCode != null) {
                		try {
							PreparedStatement deleteTrainSetstmt = connection.prepareStatement("DELETE FROM Controller WHERE product_code='" + selectedValueProductCode + "'");
							PreparedStatement deleteProductstmt = connection.prepareStatement("DELETE FROM Products WHERE product_code='" + selectedValueProductCode + "'");
                			int rowsUpdated = deleteTrainSetstmt.executeUpdate();
                			deleteProductstmt.executeUpdate();
                			if (rowsUpdated == 1) {
    			                System.out.println("Product successfully deleted.");
    			                TrainSetVector.clear();
    			                productInformationVector.clear();
    			                trainSetList.clearSelection();
    			                productInformationList.clearSelection();
    			                getAllTrainSetstmt = connection.createStatement();
    			                //Get All Track Products
    			                TrainSetListResultSet = getAllTrainSetstmt.executeQuery("SELECT * FROM Products INNER JOIN Train_Set ON Products.product_code = Train_Set.product_code");
    			                
    			                while (TrainSetListResultSet.next()) {
    			                    String row = TrainSetListResultSet.getString("product_code") + ", " + TrainSetListResultSet.getString("controller_product_code" + ", " + TrainSetListResultSet.getString("era_code"));
    			                    String productRow = TrainSetListResultSet.getString("product_name") + ", £" + TrainSetListResultSet.getString("retail_price") + ", " + TrainSetListResultSet.getString("stock") + ", " + TrainSetListResultSet.getString("gauge") + ", " + TrainSetListResultSet.getString("scale");
    			                    productInformationVector.add(productRow);
    			                    TrainSetVector.add(row);
    			                }
    			                JList<String> trainList = new JList<String>(TrainSetVector);
    			                trainList.setSelectedIndex(0);
    			                selectedValue = trainList.getSelectedValue();
    	                        productInformationList.setSelectedIndex(0);
    	                        selectedValueInformation = productInformationList.getSelectedValue();
    			                if (selectedValue != null) {
    			                    int firstCommaIndex = selectedValue.indexOf(",");
    			                    selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
    			                    int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
    			                    selectedValueControllerProductCode = selectedValue.substring(firstCommaIndex + 2,  secondCommaIndex);
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
    			                    
    			                    
    			                    
    			                    
    			                    //DISPLAYING SELECTED TRACK DETAILS
    			                    productName.setText("Product Name: " + selectedValueName);
    			                    productCode.setText("Product Code: " + selectedValueProductCode);
    			                    controllerProductCode.setText("Controller Code: " + selectedValueControllerProductCode);
    			                    productCost.setText("Product Cost: " + selectedValueCost);
    			                    productStock.setText("Product Stock: " + selectedValueStock);
    			                    productGauge.setText("Product Gauge: " + selectedValueGauge);
    			                    productScale.setText("Product Scale: " + selectedValueScale);
    				                
    				                
    				                repaint();
    			                } else {
    			                	productCode.setText("Product Code: ");
    			                	controllerProductCode.setText("Controller Code: ");
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
                selectedValueControllerProductCode = selectedValue.substring(firstCommaIndex + 2,  secondCommaIndex);
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
                
                //DISPLAYING SELECTED TRACK DETAILS
                productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: " + selectedValueProductCode);
                controllerProductCode.setText("Control Type: " + selectedValueControllerProductCode);
                productCost.setText("Product Cost: " + selectedValueCost);
                productStock.setText("Product Stock: " + selectedValueStock);
                productGauge.setText("Product Gauge: " + selectedValueGauge);
                productScale.setText("Product Scale: " + selectedValueScale);
            } else {
            	productName.setText("Product Name: " + selectedValueName);
                productCode.setText("Product Code: ");
                controllerProductCode.setText("Controller Code: ");
                productCost.setText("Product Cost: ");
                productGauge.setText("Product Gauge: ");
                productScale.setText("Product Scale: ");
            }
            

            
            
            trainSetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            trainSetList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        selectedValue = trainSetList.getSelectedValue();
                        selectedIndex = trainSetList.getSelectedIndex();
                        productInformationList.setSelectedIndex(selectedIndex);
                        selectedValueInformation = productInformationList.getSelectedValue();
                        if (selectedValue != null) {
                            int firstCommaIndex = selectedValue.indexOf(",");
                            selectedValueProductCode = selectedValue.substring(0, firstCommaIndex);
                            int secondCommaIndex = selectedValue.indexOf(",", firstCommaIndex + 1);
                            selectedValueControllerProductCode = selectedValue.substring(firstCommaIndex + 2,  secondCommaIndex);
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
                            
                            //New Label Values
                            productName.setText("Product Name: " + selectedValueName);
                            productCode.setText("Product Code: " + selectedValueProductCode);
                            controllerProductCode.setText("Control Type: " + selectedValueControllerProductCode);
                            productCost.setText("Product Cost: " + selectedValueCost);
                            productStock.setText("Product Stock: " + selectedValueStock);
                            productGauge.setText("Product Gauge: " + selectedValueGauge);
                            productScale.setText("Product Scale: " + selectedValueScale);
   
                            
                        }
                    }
                }
            });
            


            
            //Add List to Scrollable Pane
            JScrollPane staffScrollableList = new JScrollPane(trainSetList);
            //Add Items to Demote User Panel 
            trainSetListPanel.add(trainSetListLabel, BorderLayout.NORTH);
            trainSetListPanel.add(staffScrollableList, BorderLayout.WEST);
            //Add Items to AddOrDelete Product Panel
            addOrDeletePanel.add(addProductButton);
            addOrDeletePanel.add(deleteProductButton);
            
            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    
    