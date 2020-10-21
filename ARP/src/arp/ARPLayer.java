package arp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// ARP �닔�뻾

public class ARPLayer implements BaseLayer{
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	ARPHeader m_sHeader = new ARPHeader();
	
	// 罹먯떆�뀒�씠釉� �뾽�뜲�씠�듃瑜� �쐞�븳 �젅�씠�뼱 �꽕�젙
	public static ArpAppLayer appLayer;
	public void setArpAppLayer(ArpAppLayer Layer) {
		appLayer = Layer;
	}
	
	// ARP Cache Table �깮�꽦
	public static ArrayList<ARPCache> cache_table = new ArrayList<ARPCache>();
	public static ArrayList<Proxy> proxyEntry = new ArrayList<Proxy>();
	
	public ARPLayer(String pName) {
		pLayerName = pName;
	}
	
	//arp �뿤�뜑
	private static class ARPHeader{
		byte[] hardType = new byte[2];
		byte[] protType = new byte[2];
		byte hardSize;
		byte protSize;
		byte[] op = new byte[2];
		byte[] srcMac = new byte[6];
		byte[] srcIp = new byte[4];
		byte[] dstMac = new byte[6];
		byte[] dstIp = new byte[4];

		public ARPHeader() {
			hardType[1] = (byte)0x01;
			protType[0] = (byte)0x08;
			hardSize = 6;
			protSize = 4;
		}
		
		public void setOp(byte[] op) {
			this.op = op;
		}
		public void setSrcMac(byte[] mac) {
			this.srcMac = mac;
		}
		public void setSrcIp(byte[] ip) {
			this.srcIp = ip;
		}
		public void setDstMac(byte[] mac) {
			this.dstMac = mac;
		}
		public void setDstIp(byte[] ip) {
			this.dstIp = ip;
		}
	}
	
	//arp header�뿉 ip�� mac二쇱냼 �꽭�똿
	public void setSrcIp(byte[] ip) {
		m_sHeader.srcIp = ip;
	}
	public void setSrcMac(byte[] mac) {
		m_sHeader.srcMac = mac;
	}
	public void setDstIp(byte[] ip) {
		m_sHeader.dstIp = ip;
	}
	public void setDstMac(byte[] mac) {
		m_sHeader.dstMac = mac;
	}
	
	//�뜲�씠�꽣�뿉 header 遺숈씠湲�
	public byte[] ObjToByte(ARPHeader Header, byte[] input, int length) {
		//28byte�쓽 arp�뿤�뜑 遺숈씠湲�
		byte[] buf = new byte[length+28];
		
		for(int i = 0; i < 28; i++) {
			if(i == 0 || i == 1) {
				buf[i] = Header.hardType[i];
			}
			else if (i==2 || i==3) {
				buf[i] = Header.protType[i-2];
			}
			else if (i == 4) {
				buf[i] = Header.hardSize;
			}
			else if (i == 5) {
				buf[i] = Header.protSize;
			}
			else if (i == 6 || i == 7) {
				buf[i] = Header.op[i-6];
			}
			else if (8 <= i && i <= 13) {
				buf[i] = Header.srcMac[i-8];
			}
			else if (14 <= i && i <= 17) {
				buf[i] = Header.srcIp[i-14];
			}
			else if (18 <= i && i <= 23) {
				buf[i] = Header.dstMac[i-18];
			}
			else if (24 <= i && i <= 27) {
				buf[i] = Header.dstIp[i-24];
			}
		}
		System.arraycopy(input, 0, buf, 28, length);
		return buf;
	}
	
	//send
	public boolean Send(byte[] input, int length) {
		m_sHeader.op[0] = (byte)0x00;
		m_sHeader.op[1] = (byte)0x01;
		
		//ARP Cache List�뿉 �긽��諛� ip瑜� 異붽�, mac�� 0x00000000濡�, status�뒗 false濡� �몴�떆
		//cache �뀒�씠釉붿뿉 異붽�
		if(getCache(m_sHeader.dstIp) == null) {
			byte[] tempMac = new byte[6];
			ARPCache arpcache = new ARPCache(m_sHeader.dstIp, tempMac, false);
			addCacheTable(arpcache);
			
			// AppLayer�뿉�꽌 罹먯떆�뀒�씠釉� �뾽�뜲�씠�듃
			updateCacheTable();
		}
		
		byte[] bytes = ObjToByte(m_sHeader, input, length);
		GetUnderLayer().Send(bytes, bytes.length);
		return true;
	}
	
	//receive
	public boolean Receive(byte[] input) {
		//input[7]�� op肄붾뱶�쓽 �뮮 �옄由�
		
		//op媛� 0x01�씠硫� arp request
		if(input[7] == 0x01) {
			//srcIp, srcMac�� 蹂대궦�궗�엺�쓽 Ip�� Mac, dstIp�뒗 諛쏆��궗�엺�쓽 Ip
			byte[] srcIp = new byte[4];
			byte[] srcMac = new byte[6];
			byte[] dstIp = new byte[4];
			
			System.arraycopy(input, 8, srcMac, 0, 6);
			System.arraycopy(input, 14, srcIp, 0, 4);
			System.arraycopy(input, 24, dstIp, 0, 4);
			
			ARPCache tempARP = getCache(srcIp);
			if(tempARP == null) {
				ARPCache arpCache = new ARPCache(srcIp, srcMac, true);
				addCacheTable(arpCache);
			}
			else {
				if(tempARP.status == false) {
					tempARP.status = true;
					tempARP.mac = srcMac;
				}
			}
			updateCacheTable();

			//�굹�뿉寃� �삩 寃껋씠�씪硫� src, dst瑜� �뒪�솑�븯怨� op瑜� 0x02濡� 諛붽씔 �썑 �옱�쟾�넚
			if(Arrays.equals(dstIp, m_sHeader.srcIp)) {
				// 釉뚮줈�뱶罹먯뒪�듃媛� �븘�땶 �듅�젙 紐⑹쟻吏�濡� 媛��빞�븿
				//System.arraycopy(input, 15, m_sHeader.srcMac, 0, 6);
				input[7] = (byte)0x02;
				src_dst_swap(input);
				System.arraycopy(m_sHeader.srcMac, 0, input, 8, 6);
				System.arraycopy(m_sHeader.srcIp, 0, input, 14, 4);		
						
				GetUnderLayer().Send(input, input.length);
			}
			//Proxy ARP �뀒�씠釉붾룄 �솗�씤
			else {
				Iterator<Proxy> iter = proxyEntry.iterator();
				
				while(iter.hasNext()) {
					Proxy proxy = iter.next();
					if(Arrays.equals(dstIp,  proxy.ip)) {
						
						//System.arraycopy(input,  15,  m_sHeader.srcMac,  0, 6);
						input[7] = (byte)0x02;
						src_dst_swap(input);
						System.arraycopy(m_sHeader.srcMac, 0, input, 8, 6);	
						
						GetUnderLayer().Send(input, input.length);
						break;
					}
				}
				updateProxyEntry();
			}
		}
		
		//op媛� 0x02�씠硫� arp reply
		else if(input[7] == 0x02) {
			byte[] srcIp = new byte[4];
			byte[] srcMac = new byte[6];
			
			System.arraycopy(input, 8, srcMac, 0, 6);
			System.arraycopy(input, 14, srcIp, 0, 4);
			
			ARPCache tempARP = getCache(srcIp);
			if(tempARP != null) {
				tempARP.status = true;
				tempARP.mac = srcMac;
			}
			else if(tempARP == null) {
				ARPCache addARP = new ARPCache(srcIp, srcMac, true);
				addCacheTable(addARP);
			}
			
			// AppLayer�뿉�꽌 罹먯떆�뀒�씠釉� �뾽�뜲�씠�듃
			updateCacheTable();
		}
		
		return true;
	}
	
	
	@Override
    public String GetLayerName() {
        // TODO Auto-generated method stub
        return pLayerName;
    }

    @Override
    public BaseLayer GetUnderLayer() {
        // TODO Auto-generated method stub
        if (p_UnderLayer == null)
            return null;
        return p_UnderLayer;
    }

    @Override
    public BaseLayer GetUpperLayer(int nindex) {
        // TODO Auto-generated method stub
        if (nindex < 0 || nindex > nUpperLayerCount || nUpperLayerCount < 0)
            return null;
        return p_aUpperLayer.get(nindex);
    }

    @Override
    public void SetUnderLayer(BaseLayer pUnderLayer) {
        // TODO Auto-generated method stub
        if (pUnderLayer == null)
            return;
        this.p_UnderLayer = pUnderLayer;
    }

    @Override
    public void SetUpperLayer(BaseLayer pUpperLayer) {
        // TODO Auto-generated method stub
        if (pUpperLayer == null)
            return;
        this.p_aUpperLayer.add(nUpperLayerCount++, pUpperLayer);
    }

    @Override
    public void SetUpperUnderLayer(BaseLayer pUULayer) {
        this.SetUpperLayer(pUULayer);
        pUULayer.SetUnderLayer(this);
    }

    public byte[] src_dst_swap(byte[] input) {
    	byte[] src = new byte[10];
    	byte[] dst = new byte[10];
    	
    	System.arraycopy(input, 8, src, 0, 10);
    	System.arraycopy(input, 18, dst, 0, 10);

    	System.arraycopy(dst, 0, input, 8, 10);
    	System.arraycopy(src, 0, input, 18, 10);
    	
    	return input;
    }
    
    public class ARPCache{
    	// ip二쇱냼, mac二쇱냼, status
    	public byte[] ip = new byte[4];
    	public byte[] mac = new byte[6];
    	public boolean status;
    	
    	public ARPCache(byte[] ipAddress, byte[] macAddress, boolean status) {
    		this.ip = ipAddress;
    		this.mac = macAddress;
    		this.status = status;
    	}
    }
    
    public void addCacheTable(ARPCache cache) {
    	cache_table.add(cache);
    	updateCacheTable();
    }
    public void cacheRemoveAll() {
        cache_table.clear();
        updateCacheTable();
    }
    public void cacheRemove(byte[] ip) {
    	Iterator<ARPCache> iter = cache_table.iterator();
    	
    	while(iter.hasNext()) {
    		ARPCache cache = iter.next();
    		if(Arrays.equals(ip, cache.ip)) {
    			iter.remove();
    		}
    	}
    	updateCacheTable();
    }
    public ARPCache getCache(byte[] ip) {
    	Iterator<ARPCache> iter = cache_table.iterator();
    	while(iter.hasNext()) {
    		ARPCache cache = iter.next();
    		if(Arrays.equals(ip, cache.ip)) {
    			return cache;
    		}
    	}
    	return null;
    }
    
    
    public class Proxy{
    	public byte[] ip = new byte[4];
    	public byte[] mac = new byte[6];
    	
    	public Proxy(byte[] ip, byte[] mac) {
    		this.ip = ip;
    		this.mac = mac;
    	}
    }
    
    public Proxy getProxy(byte[] ip) {
    	Iterator<Proxy> iter = proxyEntry.iterator();
    	while(iter.hasNext()) {
    		Proxy proxy = iter.next();
    		if(Arrays.equals(ip, proxy.ip)) {
    			return proxy;
    		}
    	}
    	return null;
    }
    
    public void proxyRemove(byte[] ip) {
    	Iterator<Proxy> iter = proxyEntry.iterator();
    	
    	while(iter.hasNext()) {
    		Proxy proxy = iter.next();
    		if(Arrays.equals(ip, proxy.ip)) {
    			iter.remove();
    		}
    	}
    	updateProxyEntry();
    }
    
    public void addProxy(byte[] ip, byte[] mac) {
    	Proxy proxy = new Proxy(ip, mac);
    	proxyEntry.add(proxy);
    	updateProxyEntry();
    }
    
    
    public void updateCacheTable() {
    	appLayer.updateARPCacheTable(cache_table);
    }
    
    public void updateProxyEntry() {
    	appLayer.updateProxyEntry(proxyEntry);
    }
}
