package arp;

import java.util.ArrayList;

public class TCPLayer implements BaseLayer{
	public int nUpperLayerCount = 0;
    public String pLayerName = null;
    public BaseLayer p_UnderLayer = null;
    public ArrayList<BaseLayer> p_aUpperLayer = new ArrayList<BaseLayer>();
    
    _TCP_HEADER m_tHeader = new _TCP_HEADER();
    int HeaderSize = 0;

    private class _TCP_HEADER {
    	//헤더 뭐넣어야 할지 몰겠음... ㅠㅠ
    }
    
    private byte[] ObjToByte(_TCP_HEADER Header, byte[] input, int length) {
        //헤더 붙이는 곳. 모르겠음...
    	return input;
    }
    public byte[] RemoveHeader(byte[] input, int length) {
    	int inputLength = input.length;
        byte[] buf = new byte[inputLength - HeaderSize];

        // mac 주소에 관한 정보를 삭제한다.
        for (int i = HeaderSize; i < inputLength; i++) {
            buf[i - HeaderSize] = input[i];
        }
        // 삭제한 정보를 반환한다.
        return buf;
    }
    private byte[] intToByte2(int value) {
        byte[] temp = new byte[2];
        temp[0] |= (byte) ((value & 0xFF00) >> 8);
        temp[1] |= (byte) (value & 0xFF);

        return temp;
    }
    
    
	@Override
	public boolean Send(byte[] input, int length) {
		
		byte[] bytes = ObjToByte(m_tHeader, input, length);
		this.GetUnderLayer().Send(bytes, length + HeaderSize);
		return true;
	}
    @Override
    public synchronized boolean Receive(byte[] input) {
    	// TODO Auto-generated method stub
    	return BaseLayer.super.Receive(input);
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
        // nUpperLayerCount++;

    }

    @Override
    public void SetUpperUnderLayer(BaseLayer pUULayer) {
        this.SetUpperLayer(pUULayer);
        pUULayer.SetUnderLayer(this);
    }
}
