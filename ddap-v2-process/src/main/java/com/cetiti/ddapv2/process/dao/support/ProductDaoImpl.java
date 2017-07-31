package com.cetiti.ddapv2.process.dao.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cetiti.ddapv2.process.dao.ProductDao;
import com.cetiti.ddapv2.process.model.DBModel;
import com.cetiti.ddapv2.process.model.Product;

@Repository
public class ProductDaoImpl implements ProductDao {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Override
	public int insertProduct(Product product) {
		if(null==product){
			return 0;
		}
		product.setCreateTime(new Date());
		product.setDataState(DBModel.STATE_NEW);
		return this.jdbcTemplate.update("insert into ddap_product (id, name, description, "
				+ "protocol, attributes, product_key, product_secret, data_state, owner, create_time) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[]{
						product.getId(),
						product.getName(),
						product.getDescription(),
						product.getProtocol(),
						product.getAttributes(),
						product.getProductKey(),
						product.getProductSecret(),
						String.valueOf(product.getDataState()),
						product.getOwner(),
						product.getCreateTime()
				});
	}

	@Override
	public int deleteProduct(String productId) {
		return this.jdbcTemplate.update("delete from ddap_product where id = ?", productId);
	}

	@Override
	public int updateProduct(Product product) {
		Object[] update = buildUpdateSql(product);
		if(null==update){
			return 0;
		}
		String sql = (String)update[update.length-1];
		Object[] values = new Object[update.length-1];
		System.arraycopy(update, 0, values, 0, update.length-1);
		return this.jdbcTemplate.update(sql, values);
	}

	@Override
	public Product selectProduct(String productId) {
		return this.jdbcTemplate.queryForObject("select "
				+ "id, name, description, protocol, attributes, product_key, product_secret, data_state, owner "
				+ "from ddap_product where id = ?", 
				new Object[]{productId},  new ProductMapper());
	}

	@Override
	public List<Product> selectProductList() {
		return this.jdbcTemplate.query("select "
				+ "id, name, description, protocol, attributes, product_key, product_secret, data_state, owner "
				+ "from ddap_product", new ProductMapper());
	}
	
	private static final class ProductMapper implements RowMapper<Product> {

	    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Product product = new Product();
	    	product.setId(rs.getString("id"));
	    	product.setName(rs.getString("name"));
	    	product.setDescription("description");
	    	product.setProtocol(rs.getString("protocol"));
	    	product.setAttributes("attributes");
	    	product.setProductKey(rs.getString("product_key"));
	    	product.setProductSecret(rs.getString("product_secret"));
	    	String state = rs.getString("data_state");
	        if(null!=state){
	        	product.setDataState(state.charAt(0));
	        }
	        product.setOwner(rs.getString("owner"));
	        return product;
	    }
	}
	
	private Object[] buildUpdateSql(Product product) {
		if(null==product||null==product.getId()){
			return null;
		}
		StringBuilder update = new StringBuilder();
		Object[] values = new Object[20];
		int i = 0;
		update.append("update ddap_product set update_time = ?");
		values[i] = new Date(); 
		i++;
		if(!StringUtils.isEmpty(product.getDataState())){
			update.append(", data_state = ?");
			values[i] = String.valueOf(product.getDataState());
			i++;
		}
		if(StringUtils.hasText(product.getName())){
			update.append(", name = ?");
			values[i] = product.getName();
			i++;
		}
		if(StringUtils.hasText(product.getDescription())){
			update.append(", description = ?");
			values[i] = product.getDescription();
			i++;
		}
		if(StringUtils.hasText(product.getProtocol())){
			update.append(", protocol = ?");
			values[i] = product.getProtocol();
			i++;
		}
		if(StringUtils.hasText(product.getAttributes())){
			update.append(", attributes = ?");
			values[i] = product.getAttributes();
			i++;
		}
		if(StringUtils.hasText(product.getProductKey())){
			update.append(", product_key = ?");
			values[i] = product.getProductKey();
			i++;
		}
		if(StringUtils.hasText(product.getProductSecret())){
			update.append(", product_secret = ?");
			values[i] = product.getProductSecret();
			i++;
		}
		if(StringUtils.hasText(product.getOwner())){
			update.append(", owner = ?");
			values[i] = product.getOwner();
			i++;
		}
		values[i] = product.getId();
		i++;
		
		update.append(" where id = ?");
		values[i] = update.toString();
		i++;
		
		Object[] retn = new Object[i];
		System.arraycopy(values, 0, retn, 0, i);
		
		return retn;
	}

}
