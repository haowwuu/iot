package com.cetiti.ddapv2.process.dao.support;

import static org.junit.Assert.*;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cetiti.ddapv2.process.dao.DeviceDao;
import com.cetiti.ddapv2.process.model.Device;
import com.cetiti.ddapv2.process.util.EncryptUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/acceptors.xml", "classpath:spring/store.xml"})
public class DeviceDaoImplTest extends AbstractJUnit4SpringContextTests{
	
	@Resource
	private DeviceDao deviceDao;

	@Before
	public void setUp() throws Exception {
	}

	@Ignore
	public void testInsertDevice() {
		Device device = new Device();
		device.setName("sensor2");
		device.setDescription("ddap sensor");
		device.setProductId("P1501488322947");
		device.setDeviceStatus(Device.STATUS_ONLINE);
		device.setDeviceKey(EncryptUtil.generateDeviceKey(device));
		device.setDeviceSecret(EncryptUtil.generateDeviceSecret(device));
		device.setOwner("admin");
		deviceDao.insertDevice(device);
	}

	@Ignore
	public void testDeleteDevice() {
		deviceDao.deleteDevice("D1501489788148");
	}

	@Ignore
	public void testUpdateDevice() {
		Device device = new Device();
		device.setId("D1501489788148");
		device.setName("sensor2u");
		device.setDescription("ddap sensoru");
		device.setProductId("P1501488322947");
		device.setDeviceStatus(Device.STATUS_OFFLINE);
		device.setDeviceKey(EncryptUtil.generateDeviceKey(device));
		device.setDeviceSecret(EncryptUtil.generateDeviceSecret(device));
		device.setOwner("adminu");
		deviceDao.updateDevice(device);
	}

	@Ignore
	public void testSelectDeviceList() {
		System.out.println(deviceDao.selectDevice("D1501489788148"));
	}

	@Ignore
	public void testSelectDevice() {
		deviceDao.selectDeviceList().stream().forEach(System.out::println);
	}

}