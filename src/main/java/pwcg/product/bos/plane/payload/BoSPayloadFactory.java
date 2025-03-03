package pwcg.product.bos.plane.payload;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.plane.BoSPlaneAttributeFactory;
import pwcg.product.bos.plane.BosPlaneAttributeMapping;
import pwcg.product.bos.plane.payload.aircraft.A20BPayload;
import pwcg.product.bos.plane.payload.aircraft.B25DRAFPayload;
import pwcg.product.bos.plane.payload.aircraft.Bf109E7Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf109F2Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf109F4Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf109G14Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf109G2Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf109G4Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf109G6LatePayload;
import pwcg.product.bos.plane.payload.aircraft.Bf109G6Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf109K4Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf110E2Payload;
import pwcg.product.bos.plane.payload.aircraft.Bf110G2Payload;
import pwcg.product.bos.plane.payload.aircraft.C47Payload;
import pwcg.product.bos.plane.payload.aircraft.Fw190A3Payload;
import pwcg.product.bos.plane.payload.aircraft.Fw190A5Payload;
import pwcg.product.bos.plane.payload.aircraft.Fw190A6Payload;
import pwcg.product.bos.plane.payload.aircraft.Fw190A8Payload;
import pwcg.product.bos.plane.payload.aircraft.Fw190D9Payload;
import pwcg.product.bos.plane.payload.aircraft.He111H16Payload;
import pwcg.product.bos.plane.payload.aircraft.He111H6Payload;
import pwcg.product.bos.plane.payload.aircraft.Hs129B2Payload;
import pwcg.product.bos.plane.payload.aircraft.HurricaneMkIIPayload;
import pwcg.product.bos.plane.payload.aircraft.I16Type24Payload;
import pwcg.product.bos.plane.payload.aircraft.IL2M41Payload;
import pwcg.product.bos.plane.payload.aircraft.IL2M42Payload;
import pwcg.product.bos.plane.payload.aircraft.IL2M43Payload;
import pwcg.product.bos.plane.payload.aircraft.Ju52Payload;
import pwcg.product.bos.plane.payload.aircraft.Ju87D3Payload;
import pwcg.product.bos.plane.payload.aircraft.Ju88A4Payload;
import pwcg.product.bos.plane.payload.aircraft.La5FNS2Payload;
import pwcg.product.bos.plane.payload.aircraft.La5S8Payload;
import pwcg.product.bos.plane.payload.aircraft.Lagg3S29Payload;
import pwcg.product.bos.plane.payload.aircraft.Ma202Ser8Payload;
import pwcg.product.bos.plane.payload.aircraft.Me262APayload;
import pwcg.product.bos.plane.payload.aircraft.MiG3Ser24Payload;
import pwcg.product.bos.plane.payload.aircraft.P38J25Payload;
import pwcg.product.bos.plane.payload.aircraft.P39L1Payload;
import pwcg.product.bos.plane.payload.aircraft.P40E1Payload;
import pwcg.product.bos.plane.payload.aircraft.P47D22Payload;
import pwcg.product.bos.plane.payload.aircraft.P47D28Payload;
import pwcg.product.bos.plane.payload.aircraft.P51D15Payload;
import pwcg.product.bos.plane.payload.aircraft.Pe2S35Payload;
import pwcg.product.bos.plane.payload.aircraft.Pe2S87Payload;
import pwcg.product.bos.plane.payload.aircraft.SpitfireMkIXePayload;
import pwcg.product.bos.plane.payload.aircraft.SpitfireMkVbPayload;
import pwcg.product.bos.plane.payload.aircraft.SpitfireMkXIVPayload;
import pwcg.product.bos.plane.payload.aircraft.TempestMKVS2Payload;
import pwcg.product.bos.plane.payload.aircraft.TyphoonMkIbPayload;
import pwcg.product.bos.plane.payload.aircraft.U2VSPayload;
import pwcg.product.bos.plane.payload.aircraft.Yak1S127Payload;
import pwcg.product.bos.plane.payload.aircraft.Yak1S69Payload;
import pwcg.product.bos.plane.payload.aircraft.Yak7BS36Payload;
import pwcg.product.bos.plane.payload.aircraft.Yak9S1Payload;
import pwcg.product.bos.plane.payload.aircraft.Yak9TS1Payload;

public class BoSPayloadFactory implements IPayloadFactory
{
	public IPlanePayload createPlanePayload(String planeTypeName) throws PWCGException 
	{
		PlaneType planeType = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeName);
		BosPlaneAttributeMapping attributeMapping = BoSPlaneAttributeFactory.createPlaneAttributeMap(planeTypeName);
	    
        if (attributeMapping == BosPlaneAttributeMapping.BF109_E7)
        {
            return new Bf109E7Payload(planeType);
        }
        if (attributeMapping == BosPlaneAttributeMapping.BF109_F2)
        {
            return new Bf109F2Payload(planeType);
        }
        if (attributeMapping == BosPlaneAttributeMapping.BF109_F4)
        {
            return new Bf109F4Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF109_G2)
        {
            return new Bf109G2Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF109_G4)
        {
            return new Bf109G4Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF109_G6)
        {
            return new Bf109G6Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF109_G6_LATE)
        {
            return new Bf109G6LatePayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF109_G14)
        {
            return new Bf109G14Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF109_K4)
        {
            return new Bf109K4Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF110_E2)
        {
            return new Bf110E2Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.BF110_G2)
        {
            return new Bf110G2Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.FW190_A3)
        {
            return new Fw190A3Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.FW190_A5)
        {
            return new Fw190A5Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.FW190_A6)
        {
            return new Fw190A6Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.FW190_A8)
        {
            return new Fw190A8Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.FW190_D9)
        {
            return new Fw190D9Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.HE111_H6)
        {
            return new He111H6Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.HE111_H16)
        {
            return new He111H16Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.HS129_B2S)
        {
            return new Hs129B2Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.JU52)
        {
            return new Ju52Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.JU87_D3)
        {
            return new Ju87D3Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.JU88_A4)
        {
            return new Ju88A4Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.Ma202_SER8)
        {
            return new Ma202Ser8Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.ME262_A)
        {
            return new Me262APayload(planeType);
        }

        // Russian
        else if (attributeMapping == BosPlaneAttributeMapping.U2_VS)
        {
            return new U2VSPayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.I16_T24)
        {
            return new I16Type24Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.IL2_M41)
        {
            return new IL2M41Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.IL2_M42)
        {
            return new IL2M42Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.IL2_M43)
        {
            return new IL2M43Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.LAGG3_S29)
        {
            return new Lagg3S29Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.LA5_S8)
        {
            return new La5S8Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.LA5N_S2)
        {
            return new La5FNS2Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.MIG3_S24)
        {
            return new MiG3Ser24Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.P38_J25)
        {
            return new P38J25Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.P39_L1)
        {
            return new P39L1Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.P40_E1)
        {
            return new P40E1Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.P47_D22)
        {
            return new P47D22Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.P47_D28)
        {
            return new P47D28Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.P51_D15)
        {
            return new P51D15Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.PE2_S35)
        {
            return new Pe2S35Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.PE2_S87)
        {
            return new Pe2S87Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.A20B)
        {
            return new A20BPayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.B25)
        {
            return new B25DRAFPayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.C47)
        {
            return new C47Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.HURRICANE_MKII)
        {
            return new HurricaneMkIIPayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.SPITFIRE_MKVB)
        {
            return new SpitfireMkVbPayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.SPITFIRE_MKIXE)
        {
            return new SpitfireMkIXePayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.SPITFIRE_MKXIV)
        {
            return new SpitfireMkXIVPayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.TEMPEST_MKVS2)
        {
            return new TempestMKVS2Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.TYPHOON_MKIB)
        {
            return new TyphoonMkIbPayload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.YAK1_S69)
        {
            return new Yak1S69Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.YAK1_S127)
        {
            return new Yak1S127Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.YAK7B_S36)
        {
            return new Yak7BS36Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.YAK9_S1)
        {
            return new Yak9S1Payload(planeType);
        }
        else if (attributeMapping == BosPlaneAttributeMapping.YAK9T_S1)
        {
            return new Yak9TS1Payload(planeType);
        }

        throw new PWCGException ("No payload for plane " + planeTypeName);
	}

    @Override
    public PayloadDesignation getPlanePayloadDesignation(String planeTypeName, int selectedPrimaryPayloadId) throws PWCGException
    {
        IPlanePayload planePayload = createPlanePayload(planeTypeName);
        planePayload.setSelectedPayloadId(selectedPrimaryPayloadId);
        return planePayload.getSelectedPayloadDesignation();
    }
}
