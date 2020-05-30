package com.account;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("user")
public class ShopManagement 
{
	AccountManager manage=new AccountManager();
	@POST
	@Path("signup/{name}/{owner}/{product}/{pancard}/{mobno}/{email}/{place}/{password}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse signUp(@PathParam("name")String shopName,
					   @PathParam("owner")String owner,
					   @PathParam("product")String product,
					   @PathParam("pancard")String pancard,
					   @PathParam("mobno")String mobno,
					   @PathParam("email")String email,
					   @PathParam("place")String place,
					   @PathParam("password")String password) throws ClassNotFoundException, SQLException, URISyntaxException
	{
		Shop shop=new Shop();
		shop.setShopName(shopName);
		shop.setOwner(owner);
		shop.setProduct(product);
		shop.setPancard(pancard);
		shop.setMobNo(mobno);
		shop.setEmail(email);
		shop.setPlace(place);
		shop.setPassword(password);
		return manage.signup(shop);
	}
	
	@POST
	@Path("signin/{mobno}/{password}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse signin(@PathParam("mobno")String mobNo,
								 @PathParam("password")String password) throws ClassNotFoundException, SQLException
	{
		return manage.signin(mobNo,password);
	}
	
	@POST
	@Path("additem/{itemName}/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse addItem(@PathParam("itemName")String itemName,
								  @PathParam("OrgaId")int OrgaId) throws ClassNotFoundException, SQLException
	{
		return manage.addItem(itemName,OrgaId);
	}
	
	@GET
	@Path("itemlist/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Item> itemList(@PathParam("OrgaId")int OrgaId) throws ClassNotFoundException, SQLException
	{
		return manage.itemList(OrgaId);
	}
	
	@GET
	@Path("purchase/{date}/{itemname}/{vendorname}/{quantity}/{amount}/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse purchase(@PathParam("date")String date,
								   @PathParam("itemname")String itemName,
								   @PathParam("vendorname")String vendorName,
								   @PathParam("quantity")int quantity,
								   @PathParam("amount")int amount,
								   @PathParam("OrgaId")int orgaId) throws ClassNotFoundException, SQLException
	{
		Item item=new Item();
		item.setDate(date);
		item.setItemName(itemName);
		item.setVendorName(vendorName);
		item.setQuantity(quantity);
		item.setAmount(amount);
		item.setUserId(orgaId);
		return manage.purchase(item);
	}
	
	@GET
	@Path("sale/{date}/{itemname}/{customer}/{quantity}/{amount}/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse sale(@PathParam("date")String date,
								   @PathParam("itemname")String itemName,
								   @PathParam("customer")String customer,
								   @PathParam("quantity")int quantity,
								   @PathParam("amount")int amount,
								   @PathParam("OrgaId")int orgaId) throws ClassNotFoundException, SQLException
	{
		Item item=new Item();
		item.setDate(date);
		item.setItemName(itemName);
		item.setVendorName(customer);
		item.setQuantity(quantity);
		item.setAmount(amount);
		item.setUserId(orgaId);
		System.out.println(itemName);
		return manage.sale(item);
	}
	
	@POST
	@Path("adminsignin/{username}/{password}/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse adminSignin(@PathParam("username")String userName,
									  @PathParam("password")String password,
									  @PathParam("OrgaId")int orgaId) throws ClassNotFoundException, SQLException
	{
		Admin admin = new Admin();
		admin.setUserName(userName);
		admin.setPassword(password);
		admin.setShopId(orgaId);
		return manage.adminSignin(admin);
	}
	
	@GET
	@Path("itemdetail/{itemname}/{from}/{to}/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Item> itemDetail(@PathParam("itemname")String itemName,
									 @PathParam("from")String from,
									 @PathParam("to")String to,
									 @PathParam("OrgaId")int OrgaId) throws ClassNotFoundException, SQLException
	{
		Item item=new Item();
		item.setItemName(itemName);
		item.setFrom(from);
		item.setTo(to);
		item.setUserId(OrgaId);
		return manage.itemDetail(item);
	}
	
	@GET
	@Path("stock/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Item> stock(@PathParam("OrgaId")int OrgaId) throws ClassNotFoundException, SQLException
	{
		return manage.stock(OrgaId);
	}
	
	@GET
	@Path("status/{from}/{to}/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public Item status(@PathParam("from")String from,
					   @PathParam("to")String to,
					   @PathParam("OrgaId")int OrgaId) throws ClassNotFoundException, SQLException
	{
		Item item = new Item();
		item.setFrom(from);
		item.setTo(to);
		item.setUserId(OrgaId);
		return manage.status(item);
	}
	
	@GET
	@Path("allpurchase/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Item> allPurchase(@PathParam("OrgaId")int OrgaId) throws ClassNotFoundException, SQLException
	{
		return manage.allPurchase(OrgaId);
	}
	
	@GET
	@Path("allsale/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Item> allSale(@PathParam("OrgaId")int OrgaId) throws ClassNotFoundException, SQLException
	{
		return manage.allSale(OrgaId);
	}
	
	@POST
	@Path("addvendor/{vendorName}/{OrgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse addVendor(@PathParam("vendorName")String vendorName,
									@PathParam("OrgaId")int orgaId) throws ClassNotFoundException, SQLException
	{
		Vendor vendor=new Vendor();
		vendor.setVendorName(vendorName);
		vendor.setShopId(orgaId);
		return manage.addVendor(vendor);
	}
	
	@GET
	@Path("vendorlist/{orgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Vendor> vendorList(@PathParam("orgaId")int orgaId) throws ClassNotFoundException, SQLException
	{
		return manage.vendorList(orgaId);
	}
	
	@POST
	@Path("vendoritem/{vendorname}/{itemname}/{orgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public ReturnResponse vendorItem(@PathParam("vendorname")String vendorName,
									 @PathParam("itemname")String itemName,
									 @PathParam("orgaId")int orgaId) throws ClassNotFoundException, SQLException
	{
		Vendor vendor=new Vendor();
		vendor.setVendorName(vendorName);
		vendor.setItemName(itemName);
		vendor.setShopId(orgaId);
		return manage.vendorItem(vendor);
	}
	
	@GET
	@Path("venitemlist/{vendorname}/{orgaId}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Vendor> vendorItemList(@PathParam("vendorname")String vendorName,
									   @PathParam("orgaId")int orgaId) throws ClassNotFoundException, SQLException
	{
		Vendor vendor=new Vendor();
		vendor.setVendorName(vendorName);
		vendor.setShopId(orgaId);
		return manage.vendorItemList(vendor);
	}
}
