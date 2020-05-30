package com.account;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountManager 
{
	private final static String connectionURL = "jdbc:postgresql://localhost:5432/Organization";
	private final static String userName = "postgres";
	private final static String password = "aravinth";
	
	public ReturnResponse signup(Shop detail) throws SQLException, URISyntaxException, ClassNotFoundException
	{
		Statement statement = null;
		PreparedStatement st;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from organization";
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			boolean flag = true;
			while(result.next())
			{
					if(result.getString("mobileno").equals(detail.getMobNo()))
					{
						flag = false;
						ReturnResponse retRes = new ReturnResponse();
						retRes.setRes("Already");
						return retRes;
					}
					else if(result.getString("emailid").equals(detail.getEmail()))
					{
						flag = false;
						ReturnResponse retRes = new ReturnResponse();
						retRes.setRes("Email");
						return retRes;
					}
			}
			statement.close();
			if(flag)
			{
				String query1 = "insert into organization(orga_name,place,owner,product,pancard,mobileno,emailid,password)"
								+"values(?,?,?,?,?,?,?,?)";
				st = connection.prepareStatement(query1);
				st.setString(1,detail.getShopName());
				st.setString(2,detail.getPlace());
				st.setString(3,detail.getOwner());
				st.setString(4,detail.getProduct());
				st.setString(5,detail.getPancard());
				st.setString(6,detail.getMobNo());
				st.setString(7,detail.getEmail());
				st.setString(8,detail.getPassword());
				st.executeUpdate();
				st.close();
				
				String query2 = "select*from organization";
				statement = connection.createStatement();
				result = statement.executeQuery(query2);
				int id=0;
				while(result.next())
				{
						if(result.getString("mobileno").equals(detail.getMobNo()))
						{
							id=result.getInt("Orga_Id");
						}
				}	
				statement.close();
				ReturnResponse retRes = new ReturnResponse();
				retRes.setRes("Success");
				retRes.setUserId(id);
				return retRes;
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public ReturnResponse signin(String mobNo,String pin) throws SQLException, ClassNotFoundException
	{
		Statement statement;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			System.out.println(mobNo+" "+pin);
			String query = "select*from Organization";
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			boolean flag = true;
			while(result.next())
			{
					if(result.getString("mobileNo").equals(mobNo))
					{
						if(result.getString("password").equals(pin))
						{
							System.out.println(result.getString("mobileNo")+" "+result.getString("password"));
							flag = false;
							ReturnResponse retRes = new ReturnResponse();
							retRes.setRes("success");
							retRes.setUserId(result.getInt("Orga_Id"));
							return retRes;
						}
					}
			}
			if(flag)
			{
				ReturnResponse retRes = new ReturnResponse();
				retRes.setRes("wrong");
				return retRes;
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public ReturnResponse addItem(String itemName,int OrgaId) throws ClassNotFoundException, SQLException
	{
		PreparedStatement st;
		Statement statement;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from Item where Orga_Id="+OrgaId;
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			boolean flag = true;
			while(result.next())
			{
					if(result.getString("itemName").equalsIgnoreCase(itemName))
					{
						flag = false;
						ReturnResponse retRes = new ReturnResponse();
						retRes.setRes("already");
						return retRes;
					}
			}
			if(flag)
			{
				String query1 = "insert into Item(orga_id,itemName)values(?,?)";
				st = connection.prepareStatement(query1);
				st.setInt(1,OrgaId);
				st.setString(2,itemName);
				st.executeUpdate();
				st.close();
		
				ReturnResponse retRes = new ReturnResponse();
				retRes.setRes("success");
				return retRes;
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;			
	}
	
	public List<Item> itemList(int OrgaId) throws SQLException, ClassNotFoundException
	{
		ArrayList<Item> itemList = new ArrayList<Item>();
		Statement statement = null;
		ResultSet result = null;
		itemList.clear();
		Item item;
		Connection connection = null;
 		try 
		{
 			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from item where Orga_id ="+OrgaId;
			
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			while(result.next())
			{
				item = new Item();
				item.setItemName(result.getString("itemname"));
				itemList.add(item);
			}
			return itemList;
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				statement.close();
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public ReturnResponse purchase(Item item) throws ClassNotFoundException, SQLException
	{
		PreparedStatement st;
		Statement statement;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM Vendor LEFT JOIN VendorItem "+
					  " ON Vendor.VendorId = VendorItem.VendorId where Vendor.Orga_Id="+item.getUserId();
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			boolean flag=true, flag1=true;
			int itemId = 0;
			while(result.next())
			{
				if((item.getVendorName()).equalsIgnoreCase(result.getString("vendorName")))
				{
					flag1=false;
					String query1 = "SELECT*FROM Item where ItemId="+result.getInt("ItemId");
					statement = connection.createStatement();
					ResultSet result1 = statement.executeQuery(query1);
					while(result1.next())
					{
						if(item.getItemName().equalsIgnoreCase(result1.getString("ItemName")))
						{
							flag = false;
							itemId = result.getInt("itemId");
						}
					}
				}
			}
			ReturnResponse retRes = new ReturnResponse();
			if(!flag)
			{
				String query1 = "insert into purchase(date,orga_id,itemid,vendorName,qty,amount)values(?,?,?,?,?,?)";
				st = connection.prepareStatement(query1);
				st.setDate(1,java.sql.Date.valueOf(item.getDate()));
				st.setInt(2,item.getUserId());
				st.setInt(3,itemId);
				st.setString(4,item.getVendorName());
				st.setInt(5,item.getQuantity());
				st.setInt(6,item.getAmount());
				st.executeUpdate();
				st.close();
			
				retRes.setRes("success");
				return retRes;
			}
			else if(flag1)
			{
				retRes.setRes("vendor");
				return retRes;
			}
			else if(flag)
			{
				retRes.setRes("item");
				return retRes;
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;			
	}
	
	public ReturnResponse sale(Item item) throws ClassNotFoundException, SQLException
	{
		PreparedStatement st;
		Statement statement;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from Item where Orga_Id="+item.getUserId();
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			int itemId=0;
			boolean flag = true;
			while(result.next())
			{
					System.out.println(result.getString("itemName"));
					if(result.getString("itemName").equalsIgnoreCase(item.getItemName()))
					{
						flag = false;
						itemId = result.getInt("itemId");
					}
			}
			if(!flag)
			{
				String query1 = "insert into sale(date,orga_id,itemid,customer,qty,amount)values(?,?,?,?,?,?)";
				st = connection.prepareStatement(query1);
				st.setDate(1,java.sql.Date.valueOf(item.getDate()));
				st.setInt(2,item.getUserId());
				st.setInt(3,itemId);
				st.setString(4,item.getVendorName());
				st.setInt(5,item.getQuantity());
				st.setInt(6,item.getAmount());
				st.executeUpdate();
				st.close();
		
				ReturnResponse retRes = new ReturnResponse();
				retRes.setRes("success");
				return retRes;
			}
			else
			{
				ReturnResponse retRes = new ReturnResponse();
				retRes.setRes("fail");
				return retRes;
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;			
	}
	
	public ReturnResponse adminSignin(Admin admin) throws SQLException, ClassNotFoundException
	{
		Statement statement;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from Admin where Orga_Id="+admin.getShopId();
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			boolean flag = true;
			while(result.next())
			{
					if(result.getString("username").equals(admin.getUserName()))
					{
						if(result.getString("password").equals(admin.getPassword()))
						{
							flag = false;
							ReturnResponse retRes = new ReturnResponse();
							retRes.setRes("success");
							return retRes;
						}
					}
			}
			if(flag)
			{
				ReturnResponse retRes = new ReturnResponse();
				retRes.setRes("wrong");
				return retRes;
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public List<Item> itemDetail(Item item) throws SQLException, ClassNotFoundException
	{
		Statement statement = null;
		ResultSet result;
		Connection connection = null;
		ArrayList<Item> detail = new ArrayList<Item>();
		detail.clear();
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM item where Orga_Id="+item.getUserId();
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			int itemId=0;
			while(result.next())
			{
				if(result.getString("itemName").equals(item.getItemName()))
				{
					itemId = result.getInt("itemId");
				}
			}
			statement.close();
			
			String query1 = "SELECT qty,amount FROM purchase WHERE itemId="+itemId+" and date BETWEEN "+"'"+
							 item.getFrom()+"'"+" AND "+"'"+item.getTo()+"'";
			int amo = 0;
			int qty = 0;
			statement = connection.createStatement();
			result = statement.executeQuery(query1);
			while(result.next())
			{
				amo= amo+result.getInt("amount");
				qty = qty+result.getInt("qty");
			}
			Item det = new Item();
			det.setAmount(amo);
			det.setQuantity(qty);
			detail.add(det);
			statement.close();
			
			String query2 = "SELECT qty,amount FROM sale WHERE itemId="+itemId+" and date BETWEEN "+"'"+
							item.getFrom()+"'"+" AND "+"'"+item.getTo()+"'";
			int quantity = 0;
			int amount = 0;
			statement = connection.createStatement();
			result = statement.executeQuery(query2);
			while(result.next())
			{
				quantity = quantity+result.getInt("qty");
				amount = amount+result.getInt("amount");
			}
			Item det1 = new Item();
			det1.setAmount(amount);
			det1.setQuantity(quantity);
			detail.add(det1);
			statement.close();
			
			return detail;
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public List<Item> stock(int OrgaId) throws ClassNotFoundException, SQLException
	{
		Statement statement = null;
		Connection connection = null;
		ArrayList<Item> detail = new ArrayList<Item>();
		detail.clear();
		int purchase=0;
		int sale=0;
		String itemName="";
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM item where Orga_Id="+OrgaId;
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			while(result.next())
			{
				purchase=0;
				sale=0;
				
				String query1 = "SELECT*FROM purchase where itemId="+result.getInt("itemId");
				statement = connection.createStatement();
				ResultSet result1 = statement.executeQuery(query1);
				while(result1.next())
				{
					purchase = purchase+result1.getInt("Qty");
				}
				statement.close();
				String query2 = "SELECT*FROM sale where itemId="+result.getInt("itemId");
				statement = connection.createStatement();
				ResultSet result2 = statement.executeQuery(query2);
				while(result2.next())
				{
					sale = sale+result2.getInt("Qty");
				}
				statement.close();
				String query3 = "SELECT*FROM item where itemId="+result.getInt("itemId");
				statement = connection.createStatement();
				ResultSet result3 = statement.executeQuery(query3);
				while(result3.next())
				{
					itemName = result3.getString("itemName");
				}
				int stock=purchase-sale;
				statement.close();
				Item item=new Item();
				item.setQuantity(purchase);
				item.setAmount(sale);
				item.setItemName(itemName);
				item.setStock(stock);
				detail.add(item);
			}
			statement.close();
			return detail;
		}
			
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public Item status(Item item) throws SQLException, ClassNotFoundException
	{
		Statement statement = null;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM purchase WHERE Orga_Id="+item.getUserId()+" and date BETWEEN "+"'"+
							 item.getFrom()+"'"+" AND "+"'"+item.getTo()+"'";
			int pur = 0;
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			while(result.next())
			{
				pur= pur+result.getInt("amount");
			}
			statement.close();
			
			String query1 = "SELECT*FROM sale WHERE Orga_Id="+item.getUserId()+" and date BETWEEN "+"'"+
							item.getFrom()+"'"+" AND "+"'"+item.getTo()+"'";
			int sal = 0;
			statement = connection.createStatement();
			result = statement.executeQuery(query1);
			while(result.next())
			{
				sal = sal+result.getInt("amount");
			}
			statement.close();
			
			Item stat = new Item();
			stat.setPurchase(pur);
			stat.setSale(sal);
			int amount = sal-pur;
			stat.setAmount(amount);
			if(amount>0)
			{
				stat.setStatus("Profit");
			}
			else
			{
				stat.setStatus("Loss");
			}
			return stat;
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public List<Item> allPurchase(int OrgaId) throws SQLException, ClassNotFoundException
	{
		Statement statement = null;
		Connection connection = null;
		ArrayList<Item> all = new ArrayList<Item>();
		all.clear();
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM purchase WHERE Orga_Id="+OrgaId;
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			while(result.next())
			{
				Item item = new Item();
				String query1 = "SELECT*FROM item WHERE Orga_Id="+OrgaId;
				statement = connection.createStatement();
				ResultSet result1 = statement.executeQuery(query1);
				while(result1.next())
				{
					if((result1.getInt("itemId"))==(result.getInt("itemId")))
					{
						item.setItemName(result1.getString("itemName"));
					}
				}
				
				String date = sdfr.format(result.getDate("date"));
				item.setDate(date);
				item.setVendorName(result.getString("vendorName"));
				item.setQuantity(result.getInt("qty"));
				item.setAmount(result.getInt("amount"));
				all.add(item);
			}
			statement.close();
			return all;
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public List<Item> allSale(int OrgaId) throws SQLException, ClassNotFoundException
	{
		Statement statement = null;
		Connection connection = null;
		ArrayList<Item> all = new ArrayList<Item>();
		all.clear();
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM sale WHERE Orga_Id="+OrgaId;
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			while(result.next())
			{
				Item item = new Item();
				String query1 = "SELECT*FROM item WHERE Orga_Id="+OrgaId;
				statement = connection.createStatement();
				ResultSet result1 = statement.executeQuery(query1);
				while(result1.next())
				{
					if((result1.getInt("itemId"))==(result.getInt("itemId")))
					{
						item.setItemName(result1.getString("itemName"));
					}
				}
				
				String date = sdfr.format(result.getDate("date"));
				item.setDate(date);
				item.setVendorName(result.getString("customer"));
				item.setQuantity(result.getInt("qty"));
				item.setAmount(result.getInt("amount"));
				all.add(item);
			}
			statement.close();
			return all;
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public ReturnResponse addVendor(Vendor vendor) throws SQLException, ClassNotFoundException
	{
		PreparedStatement st;
		Statement statement;
		ResultSet result;
		Connection connection = null;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from vendor where Orga_Id="+vendor.getShopId();
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			boolean flag = true;
			while(result.next())
			{
					if(result.getString("vendorName").equalsIgnoreCase(vendor.getVendorName()))
					{
						flag = false;
						ReturnResponse retRes = new ReturnResponse();
						retRes.setRes("already");
						return retRes;
					}
			}
			if(flag)
			{
				String query1 = "insert into Vendor(orga_id,vendorName)values(?,?)";
				st = connection.prepareStatement(query1);
				st.setInt(1,vendor.getShopId());
				st.setString(2,vendor.getVendorName());
				st.executeUpdate();
				st.close();
		
				ReturnResponse retRes = new ReturnResponse();
				retRes.setRes("success");
				return retRes;
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public List<Vendor> vendorList(int orgaId) throws ClassNotFoundException, SQLException
	{
		ArrayList<Vendor> vendorList = new ArrayList<Vendor>();
		Statement statement = null;
		ResultSet result = null;
		vendorList.clear();
		Vendor vendor;
		Connection connection = null;
 		try 
		{
 			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "select*from Vendor where Orga_id ="+orgaId;
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			while(result.next())
			{
				vendor = new Vendor();
				vendor.setVendorName(result.getString("vendorName"));
				vendorList.add(vendor);
			}
			return vendorList;
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				statement.close();
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public ReturnResponse vendorItem(Vendor vendor) throws SQLException, ClassNotFoundException
	{
		Statement statement = null;
		PreparedStatement st;
		Connection connection = null;
		boolean flag=true, flag1=true;
		try 
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM item WHERE Orga_Id="+vendor.getShopId();
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			while(result.next())
			{
				if(result.getString("itemname").equals(vendor.getItemName()))
				{
					flag=false;
					String query1 = "SELECT*FROM vendor WHERE Orga_Id="+vendor.getShopId();
					statement = connection.createStatement();
					ResultSet result1 = statement.executeQuery(query1);
					while(result1.next())
					{
						if(result1.getString("vendorname").equals(vendor.getVendorName()))
						{
							flag1=false;
							int itemId = result.getInt("ItemId");
							int vendorId = result1.getInt("VendorId");
							String query2 = "insert into vendoritem(Orga_Id,VendorId,ItemId)values(?,?,?)";
							st = connection.prepareStatement(query2);
							st.setInt(1,vendor.getShopId());
							st.setInt(2,vendorId);
							st.setInt(3,itemId);
							st.executeUpdate();
							st.close();
							ReturnResponse retres = new ReturnResponse();
							retres.setRes("success");
							return retres;	
						}
					}
					statement.close();
				}
			}
			statement.close();
			if(flag)
			{
				ReturnResponse retres = new ReturnResponse();
				retres.setRes("noitem");
				return retres;						
			}
			if(flag1)
			{
				ReturnResponse retres = new ReturnResponse();
				retres.setRes("novendor");
				return retres;						
			}
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
	
	public List<Vendor> vendorItemList(Vendor vendor) throws ClassNotFoundException, SQLException
	{
		ArrayList<Vendor> vendorList = new ArrayList<Vendor>();
		Statement statement = null;
		vendorList.clear();
		Connection connection = null;
 		try 
		{
 			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			System.out.println("Connection created successfully");
			
			String query = "SELECT*FROM Vendor LEFT JOIN VendorItem "+
						  " ON Vendor.VendorId = VendorItem.VendorId where Vendor.Orga_Id="+vendor.getShopId();
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			while(result.next())
			{
				if((vendor.getVendorName()).equalsIgnoreCase(result.getString("vendorName")))
				{
					String query1 = "SELECT*FROM Item where ItemId="+result.getInt("ItemId");
					statement = connection.createStatement();
					ResultSet result1 = statement.executeQuery(query1);
					while(result1.next())
					{
						Vendor ven = new Vendor();
						ven.setItemName(result1.getString("ItemName"));
						vendorList.add(ven);
					}
				}
			}
			return vendorList;
		}
		catch (SQLException e) 
		{	
			e.printStackTrace();
		}
		finally
		{
				statement.close();
				connection.close();
				System.out.println("Connection closed successfully");
		}
		return null;
	}
}
