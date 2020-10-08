package arp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

// ARP 수행

public class ARPLayer implements BaseLayer{
	public int nUpperLayerCount = 0;
	public String pLayerName = null;
	public BaseLayer p_UnderLayer = null;
	public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
	ARPHeader m_sHeader = new ARPHeader();
	
	public ARPLayer(String pName) {
		pLayerName = pName;
	}
	
	//arp 헤더
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
	
	//arp header에 ip와 mac주소 세팅
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
	
	
	//데이터에 header 붙이기
	public byte[] ObjToByte(ARPHeader Header, byte[] input, int length) {
		//28byte의 arp헤더 붙이기
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
		
		//ARP Cache List에 상대방 ip를 추가, mac은 ??로, status는 incomplete로 표시
		/*####################
		 * TODO
		 #####################*/
		
		
		byte[] bytes = ObjToByte(m_sHeader, input, length);
		GetUnderLayer().Send(bytes, bytes.length);
		return true;
	}
	
	//receive
	public boolean Receive(byte[] input) {
		//input[7]은 op코드의 뒷 자리
		
		
		//op가 0x01이면 arp request
		if(input[7] == 0x01) {
			//srcIp, srcMac은 보낸사람의 Ip와 Mac, dstIp는 받은사람의 Ip
			byte[] srcIp = new byte[4];
			byte[] srcMac = new byte[6];
			byte[] dstIp = new byte[4];
			
			System.arraycopy(input, 8, srcIp, 0, 6);
			System.arraycopy(input, 14, srcMac, 0, 4);
			System.arraycopy(input, 24, dstIp, 0, 4);
			
			
			//ARP Cache List에 상대방 ip와 mac을 추가
			/*###############
			 * TODO
			 ################*/
			
			if(dstIp.equals(m_sHeader.dstIp)) {
				
			}
		}
		
		//op가 0x02이면 arp reply
		if(input[7] == 0x02) {
			
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
}
