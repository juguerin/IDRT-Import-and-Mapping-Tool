package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class WizardPageOne extends WizardPage {

	private static Text ipText;
	private static Text PortText;
	private static Text DBUserText;
	private static Text DBUserPasswordText;
	private static Text DBSIDText;
	// private static Text DBSchemaText;
	// private static Label uniqueIDText;
	private static Label DBTest;
	private static Composite container;
	private static Combo comboDB;
	private static Combo comboUser;
	private static String schema;
	private Label DBTypeLabel;
	private static Combo DBTypeCombo;
	private static Label DBIntegratedSecurity;
	private static Button DBMSSQLUseWinAuth;
	private static Label DBUserPassword;
	private static Label DBUser;

	public WizardPageOne() {
		super("Target Database Setup");
		setTitle("Target Database Setup");
		setDescription("Please enter your target database connection data");
	}

	// @Override
	// public IWizardPage getPreviousPage() {
	// return new WizardPageZero();
	// }
	@Override
	public void createControl(Composite parent) {
		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("/cfg/Default.properties"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
			Properties defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

//			schema = defaultProps.getProperty("schema");
			String serverUniqueName = ServerView.getSelectedServer();
			// String serverUniqueName = null;
			// TreeViewer viewer = ServerView.getViewer();
			// if (viewer != null) {
			// if (viewer.getTree().getSelectionCount()>0){
			// if (viewer.getTree().getSelection()[0].getParentItem()!= null){
			// schema = viewer.getTree().getSelection()[0].getText();
			// serverUniqueName =
			// viewer.getTree().getSelection()[0].getParentItem().getText();
			// }
			// }
			// }
			container = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(2, true);
			container.setLayout(layout);
			Label uniqueID = new Label(container, SWT.FILL | SWT.CENTER);
			uniqueID.setText("Unique Identifier");

			// uniqueIDText = new Label(container, SWT.FILL);
			// uniqueIDText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			// false, 1, 1));
			// if(currentServer!=null)
			// uniqueIDText.setText(currentServer.getUniqueID());
			// else{
			// uniqueIDText.setText("");
			// }
			//
			comboDB = new Combo(container, SWT.NONE);
			schema = ServerView.getCurrentSchema();
			// int selectedServerIndex = 0;
			// TreeItem[] treeItems = viewer.getTree().getItems();
			// String[] serverNames = new String[treeItems.length];
			// for (int i = 0; i < treeItems.length; i++) {
			// serverNames[i] = treeItems[i].getText();
			// if (viewer.getTree().getSelectionCount()>0){
			// if(viewer.getTree().getSelection()[0].getParentItem() != null){
			// if
			// (treeItems[i].getText().equals(viewer.getTree().getSelection()[0].getParentItem().getText()))
			// selectedServerIndex=i;
			// }
			// else if (viewer.getTree().getSelection()[0] != null){
			// if
			// (treeItems[i].getText().equals(viewer.getTree().getSelection()[0].getText()))
			// selectedServerIndex=i;
			// }
			// }
			// else{
			// selectedServerIndex=0;
			// }
			// }
			// comboDB.setItems(serverNames);
			comboDB.setItems(ServerView.getCurrentServers());
			// comboDB.select(selectedServerIndex);
			comboDB.select(ServerView.getCurrentServerIndex());

			if (serverUniqueName == null) {
				System.out.println("first select: "
						+ comboDB.getItem(comboDB.getSelectionIndex()));
				serverUniqueName = comboDB.getItem(comboDB.getSelectionIndex());
			}
			Server currentServer = null;
			if (serverUniqueName != null) {
				currentServer = ServerList.getTargetServers().get(serverUniqueName);
				currentServer.toString();
			}
			comboDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			comboDB.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					Combo selectedCombo = (Combo) e.widget;
					Server selectedServer = ServerList.getTargetServers().get(
							selectedCombo.getText());
					setSelectedServer(selectedServer);

					int selectedUserIndex = 0;
					// TreeItem[] treeItems = viewer.getTree().getItems();
					// String[] serverNames = new String[treeItems.length];
					HashSet<String> users = ServerList.getUsersTargetServer(selectedServer);

					if (users != null) {
						String[] userList = new String[users.size()];

						Iterator<String> it = users.iterator();
						int i = 0;
						while (it.hasNext()) {
							userList[i] = it.next();
							if (schema.equals(userList[i])) {
								selectedUserIndex = i;
							}
							i++;
						}
						comboUser.setItems(userList);
						comboUser.select(selectedUserIndex);
					} else {
						comboUser.setItems(new String[] {});
					}
				}
			});

			// uniqueIDText.addKeyListener(new KeyListener() {
			// public void keyPressed(KeyEvent e) {
			// }
			// public void keyReleased(KeyEvent e) {
			// if (!uniqueIDText.getText().isEmpty()) {
			// setPageComplete(true);
			// }
			// else
			// setPageComplete(false);
			// }
			// });
			Label ip = new Label(container, SWT.FILL | SWT.CENTER);
			ip.setText("IP");
			// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
			ipText = new Text(container, SWT.FILL);
			ipText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
					1, 1));

			if (currentServer != null) {
				ipText.setText(currentServer.getIp());
			} else {
				ipText.setText(defaultProps.getProperty("host"));
			}
			ipText.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!ipText.getText().isEmpty()) {
						WizardPageOne.this.setPageComplete(true);
					}
				}
			});

			Label Port = new Label(container, SWT.FILL | SWT.CENTER);
			Port.setText("Port");
			// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
			PortText = new Text(container, SWT.FILL);
			PortText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			if (currentServer != null) {
				PortText.setText(currentServer.getPort());
			} else {
				PortText.setText(defaultProps.getProperty("port"));
			}

			PortText.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!PortText.getText().isEmpty()) {
						WizardPageOne.this.setPageComplete(true);
					}
				}
			});
			DBUser = new Label(container, SWT.FILL | SWT.CENTER);
			DBUser.setText("DB Username");
			// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
			DBUserText = new Text(container, SWT.FILL);
			DBUserText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			if (currentServer != null) {
				DBUserText.setText(currentServer.getUser());
			} else {
				DBUserText.setText(defaultProps.getProperty("username"));
			}

			DBUserText.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!DBUserText.getText().isEmpty()) {
						WizardPageOne.this.setPageComplete(true);
					}
				}
			});

			DBUserPassword = new Label(container, SWT.FILL | SWT.CENTER);
			DBUserPassword.setText("DB Password");
			// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
			DBUserPasswordText = new Text(container, SWT.FILL);

			DBUserPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, false, 1, 1));

			if (currentServer != null) {
				DBUserPasswordText.setText(currentServer.getPassword());
			} else {
				DBUserPasswordText.setText(defaultProps.getProperty("DBPassword"));
			}
			DBUserPasswordText.setEchoChar('*');
			DBUserPasswordText.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!DBUserPasswordText.getText().isEmpty()) {
						WizardPageOne.this.setPageComplete(true);
					}
				}
			});

			Label DBSID = new Label(container, SWT.FILL | SWT.CENTER);
			DBSID.setText("DB SID");
			// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);
			DBSIDText = new Text(container, SWT.FILL);
			DBSIDText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			if (currentServer != null) {
				DBSIDText.setText(currentServer.getSID());
//				System.out.println("GET SID: " + currentServer.getSID());
			} else {
				DBSIDText.setText(defaultProps.getProperty("database"));
			}
			DBSIDText.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (!DBSIDText.getText().isEmpty()) {
						WizardPageOne.this.setPageComplete(true);
					}
				}
			});

			
			
			Label DBSchema = new Label(container, SWT.FILL | SWT.CENTER);
			DBSchema.setText("DB Schema");
			// ipText = new Text(container, SWT.BORDER | SWT.SINGLE);

			comboUser = new Combo(container, SWT.NONE);
			DBTypeLabel = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			DBTypeLabel.setText("DB Type");
			
			DBTypeCombo = new Combo(container, SWT.READ_ONLY);
			DBIntegratedSecurity = new Label(container, SWT.SHADOW_IN | SWT.CENTER);
			DBMSSQLUseWinAuth = new Button(container, SWT.CHECK);
			DBTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			DBTypeCombo.setItems(Server.getComboItems());
			for (int i = 0; i < DBTypeCombo.getItems().length; i++) {
				if (DBTypeCombo.getItem(i).equalsIgnoreCase(currentServer.getDatabaseType())) {
					DBTypeCombo.select(i);
					break;	
				}
			}
			DBTypeCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println();
					if(DBTypeCombo.getItem(DBTypeCombo.getSelectionIndex()).equalsIgnoreCase("mssql")) {
						DBIntegratedSecurity.setEnabled(true);
						DBMSSQLUseWinAuth.setEnabled(true);
						DBUserPassword.setEnabled(!DBMSSQLUseWinAuth.getSelection());
						DBUserPasswordText.setEnabled(!DBMSSQLUseWinAuth.getSelection());
						DBUser.setEnabled(!DBMSSQLUseWinAuth.getSelection());
						DBUserText.setEnabled(!DBMSSQLUseWinAuth.getSelection());
					}
					else {
						DBUserPassword.setEnabled(true);
						DBUserPasswordText.setEnabled(true);
						DBUser.setEnabled(true);
						DBUserText.setEnabled(true);
						DBIntegratedSecurity.setEnabled(false);
						DBMSSQLUseWinAuth.setEnabled(false);
						DBMSSQLUseWinAuth.setSelection(false);

					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});

			DBIntegratedSecurity.setText("Use Windows Authentication?");
			int selectedUserIndex = 0;
			// TreeItem[] treeItems = viewer.getTree().getItems();
			// String[] serverNames = new String[treeItems.length];
			String[] userList;
			if (currentServer != null) {
				HashSet<String> users = ServerList.getUsersTargetServer(currentServer);
				if (users != null) {
					userList = new String[users.size()];

					Iterator<String> it = users.iterator();
					int i = 0;
					while (it.hasNext()) {
						userList[i] = it.next();
						if (schema.equals(userList[i])) {
							selectedUserIndex = i;
						}
						i++;
					}
				} else {
					userList = new String[0];
				}

				// for (int i = 0; i < userList.length; i++) {
				// userList[i] = users.iterator().(i).getName();
				// if (schema.equals(userList[i]))
				// selectedUserIndex=i;
				//
				// }
				comboUser.setItems(userList);
			}
			comboUser.select(selectedUserIndex);
			comboUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			// comboUser.addSelectionListener(new SelectionListener() {
			//
			// @Override
			// public void widgetSelected(SelectionEvent e) {
			// Combo selectedCombo = (Combo)e.widget;
			// Server selectedServer =
			// ServerList.getServers().get(selectedCombo.getText());
			// setSelectedServer(selectedServer);
			// }
			//
			// @Override
			// public void widgetDefaultSelected(SelectionEvent e) {
			//
			// }
			// });

			// DBSchemaText = new Text(container, SWT.FILL);
			// DBSchemaText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
			// false, 1, 1));
			//
			// DBSchemaText.setText(schema);
			//
			// DBSchemaText.addKeyListener(new KeyListener() {
			// public void keyPressed(KeyEvent e) {
			// }
			// public void keyReleased(KeyEvent e) {
			// if (!DBSchemaText.getText().isEmpty()) {
			// setPageComplete(true);
			// }
			// }
			// });

			Button buttonTestDB = new Button(container, SWT.PUSH);
			buttonTestDB.setText("Test DB connectivity");
			// Button ButtonTestDB = new Button("Test DB connectivity");
			buttonTestDB.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (IDRTImport.testDB(ipText.getText(), PortText.getText(),
							DBUserText.getText(), DBUserPasswordText.getText(),
							DBSIDText.getText(),DBTypeCombo.getText(), DBMSSQLUseWinAuth.getSelection())) {
						System.out.println("working");

						DBTest.setText("success");
						Color color_red = container.getDisplay()
								.getSystemColor(SWT.COLOR_GREEN);
						DBTest.setForeground(color_red);
						DBTest.pack();
					} else {
						// RGB value = Color.red;
						DBTest.setText("failure");
						// org.eclipse.swt.graphics.Color red = new
						// CSWT.COLOR_RED;
						Color color_red = container.getDisplay()
								.getSystemColor(SWT.COLOR_RED);
						DBTest.setForeground(color_red);
						DBTest.setToolTipText(IDRTImport.error);
						DBTest.pack();
						System.out.println("broken");
						System.out.println(IDRTImport.error);
					}
				}
			});
			DBTest = new Label(container, SWT.FILL | SWT.CENTER);
			DBTest.setText("?");

			setControl(container);

			new Label(container, SWT.NONE);
			new Label(container, SWT.NONE);
			setPageComplete(true);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @return the container1
	 */
	public static Composite getContainer1() {
		return container;
	}

	/**
	 * @return the comboDB
	 */
	public static String getCurrentServerString() {
		return comboDB.getItem(comboDB.getSelectionIndex());
	}

	/**
	 * @return the dBSchemaText
	 */
	public static String getDBSchemaText() {
		return comboUser.getText();
	}

	/**
	 * @return the dBSIDText
	 */
	public static String getDBSIDText() {
		return DBSIDText.getText();
	}

	/**
	 * @return the dBUserPasswordText
	 */
	public static String getDBUserPasswordText() {
		return DBUserPasswordText.getText();
	}

	/**
	 * @return the dBUserText
	 */
	public static String getDBUserText() {
		return DBUserText.getText();
	}

	/**
	 * @return the ipText
	 */
	public static String getIpText() {
		return ipText.getText();
	}

	/**
	 * @return the portText
	 */
	public static String getPortText() {
		return PortText.getText();
	}

	public static void setSelectedServer(Server server) {
		System.out.println("setting server: " + server.getUniqueID());

		ipText.setText(server.getIp());
		PortText.setText(server.getPort());
		DBUserText.setText(server.getUser());
		DBUserPasswordText.setText(server.getPassword());
		DBSIDText.setText(server.getSID());
	}

}
