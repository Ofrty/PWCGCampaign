package pwcg.product.fc.country;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceManager;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.AmericanColorMap;
import pwcg.gui.colors.BelgianColorMap;
import pwcg.gui.colors.FrenchColorMap;
import pwcg.gui.colors.GermanColorMap;
import pwcg.gui.colors.RAFColorMap;
import pwcg.gui.colors.RFCColorMap;
import pwcg.gui.colors.RNASColorMap;

public class FCServiceManager extends ArmedServiceManager implements IArmedServiceManager 
{
    public static int LAVIATION_MILITAIRE = 30101;
    public static int RFC = 30201;
    public static int RNAS = 30202;
    public static int RAF = 30203;
    public static int USAS = 30301;
    public static int AVIATION_MILITAIRE_BELGE = 30401;
    public static int DEUTSCHE_LUFTSTREITKRAFTE = 40101;
    
    public static String LAVIATION_MILITAIRE_NAME = "l'Aviation Militaire";
    public static String AVIATION_MILITAIRE_BELGE_NAME = "Aviation Militaire Belge";
    public static String RFC_NAME = "Royal Flying Corp";
    public static String RNAS_NAME = "Royal Naval Air Service";
    public static String RAF_NAME = "Royal Air Force";
    public static String USAS_NAME = "United States Air Service";
    public static String DEUTSCHE_LUFTSTREITKRAFTE_NAME = "Deutsche Luftstreitkrafte";
        	
    public static String LAVIATION_MILITAIRE_ICON = "ServiceFrenchArmy";
    public static String AVIATION_MILITAIRE_BELGE_ICON = "ServiceBelgianArmy";
    public static String RFC_ICON = "ServiceRFC";
    public static String RNAS_ICON = "ServiceRNAS";
    public static String RAF_ICON = "ServiceRAF";
    public static String USAS_ICON = "ServiceUSAS";
    public static String DEUTSCHE_LUFTSTREITKRAFTE_ICON = "ServiceGAS";

    private static FCServiceManager instance;
    
    public static FCServiceManager getInstance()
    {
        if (instance == null)
        {
            instance = new FCServiceManager();
            instance.initialize();

        }
        return instance;
    }
    
    private FCServiceManager ()
	{
	}

    protected void initialize() 
    {
        try
        {
            Date startDate = DateUtils.getBeginningOfGame();     
            Date endDate = DateUtils.getEndOfWar();
            Date rafStartDate = DateUtils.getRAFDate();
    
            List <ArmedService> frenchServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(FCCountry.FRANCE_CODE, frenchServices);
            
            // l'Aviation Militaire
            ArmedService frenchArmy = createServiceFrenchArmy(startDate, endDate);
            frenchServices.add(frenchArmy);
            
            // Aviation Militaire Belge
            ArmedService belgianAF = createServiceBelgianArmy(startDate, endDate);
            frenchServices.add(belgianAF);
    
            // British
            List <ArmedService> britishServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(FCCountry.BRITAIN_CODE, britishServices);
    
            // Royal Flying Corps (Britain)
            ArmedService rfc = createServiceRFC(startDate, rafStartDate);
            britishServices.add(rfc);
            
            // Royal Naval Air Service (Britain)
            ArmedService rnas = createServiceRNAS(rafStartDate);
            britishServices.add(rnas);
    
            // Royal Air Force (Britain)
            ArmedService raf = createServiceRAF(endDate, rafStartDate);
            britishServices.add(raf);           
            
            // American
            List <ArmedService> americanServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(FCCountry.USA_CODE, americanServices);
    
            // United States Air Service (USA)
            ArmedService usas = createServiceUSAS(endDate);
            americanServices.add(usas);

            // German
            List <ArmedService> germanServices = new ArrayList<ArmedService>();
            armedServicesByCountry.put(FCCountry.GERMANY_CODE, germanServices);
    
            // Deutsche Luftstreitkr�fte (Germany)
            ArmedService gas = new ArmedService();
            createServiceGermanArmy(startDate, endDate, gas);
            germanServices.add(gas);
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }
    }

    private ArmedService createServiceGermanArmy(Date startDate, Date endDate, ArmedService gas) throws PWCGException
    {
        gas.setServiceId(DEUTSCHE_LUFTSTREITKRAFTE);
        gas.setCountry(new FCCountry(FCCountry.GERMANY_CODE));
        gas.setName(DEUTSCHE_LUFTSTREITKRAFTE_NAME);
        gas.setServiceIcon(DEUTSCHE_LUFTSTREITKRAFTE_ICON);
        gas.setStartDate(startDate);
        gas.setEndDate(endDate);
        gas.setServiceColorMap(new GermanColorMap());
        gas.setGeneralRankForService("Generalleutnant");
        gas.setDailyPersonnelReplacementRate(15);
        gas.setDailyEquipmentReplacementRate(30);

        List<String> gasPics = new ArrayList<String>();
        gasPics.add("German");
        gasPics.add("German\\GAS");
        gas.setPicDirs(gasPics);
        
        gas.addServiceQuality(DateUtils.getBeginningOfWar(), 75);
        
        return gas;
    }

    private ArmedService createServiceUSAS(Date endDate) throws PWCGException
    {
        ArmedService usas = new ArmedService();
        usas.setServiceId(USAS);
        usas.setCountry(new FCCountry(FCCountry.USA_CODE));
        usas.setName(USAS_NAME);
        usas.setServiceIcon(USAS_ICON);
        usas.setStartDate(DateUtils.getDateWithValidityCheck("01/05/1918"));
        usas.setEndDate(endDate);
        usas.setServiceColorMap(new AmericanColorMap());
        usas.setGeneralRankForService("Major-General");
        usas.setDailyPersonnelReplacementRate(10);
        usas.setDailyEquipmentReplacementRate(20);

        List<String> usasPics = new ArrayList<String>();
        usasPics.add("American");
        usas.setPicDirs(usasPics);

        usas.addServiceQuality(DateUtils.getBeginningOfWar(), 40);
        
        return usas;
    }

    private ArmedService createServiceRAF(Date endDate, Date rafStartDate) throws PWCGException
    {
        ArmedService raf = new ArmedService();
        raf.setServiceId(RAF);
        raf.setCountry(new FCCountry(FCCountry.BRITAIN_CODE));
        raf.setName(RAF_NAME);
        raf.setServiceIcon(RAF_ICON);
        raf.setStartDate(rafStartDate);
        raf.setEndDate(endDate);
        raf.setServiceColorMap(new RAFColorMap());
        raf.setGeneralRankForService("Major General");
        raf.setDailyPersonnelReplacementRate(15);
        raf.setDailyEquipmentReplacementRate(30);

        List<String> rafPics = new ArrayList<String>();
        rafPics.add("British");
        rafPics.add("British\\RFC");
        raf.setPicDirs(rafPics);

        raf.addServiceQuality(DateUtils.getBeginningOfWar(), 50);
        
        return raf;
    }

    private ArmedService createServiceRNAS(Date rafStartDate) throws PWCGException
    {
        ArmedService rnas = new ArmedService();
        rnas.setServiceId(RNAS);
        rnas.setCountry(new FCCountry(FCCountry.BRITAIN_CODE));
        rnas.setName(RNAS_NAME);
        rnas.setServiceIcon(RNAS_ICON);
        rnas.setStartDate(DateUtils.getDateWithValidityCheck("01/03/1916"));
        rnas.setEndDate(rafStartDate);  // RAF start date is the RNAS end date
        rnas.setServiceColorMap(new RNASColorMap());
        rnas.setGeneralRankForService("Rear Admiral");
        rnas.setDailyPersonnelReplacementRate(8);
        rnas.setDailyEquipmentReplacementRate(16);

        List<String> rnasPics = new ArrayList<String>();
        rnasPics.add("British");
        rnasPics.add("British\\RNAS");
        rnas.setPicDirs(rnasPics);

        rnas.addServiceQuality(DateUtils.getBeginningOfWar(), 60);
        
        return rnas;
    }

    private ArmedService createServiceRFC(Date startDate, Date rafStartDate) throws PWCGException
    {
        ArmedService rfc = new ArmedService();
        rfc.setServiceId(RFC);
        rfc.setCountry(new FCCountry(FCCountry.BRITAIN_CODE));
        rfc.setName(RFC_NAME);
        rfc.setServiceIcon(RFC_ICON);
        rfc.setStartDate(startDate);
        rfc.setEndDate(rafStartDate); // RAF start date is the RFC end date
        rfc.setServiceColorMap(new RFCColorMap());
        rfc.setGeneralRankForService("Major General");
        rfc.setDailyPersonnelReplacementRate(15);
        rfc.setDailyEquipmentReplacementRate(30);

        List<String> rfcPics = new ArrayList<String>();
        rfcPics.add("British");
        rfcPics.add("British\\RFC");
        rfc.setPicDirs(rfcPics);

        rfc.addServiceQuality(DateUtils.getBeginningOfWar(), 50);
        
        return rfc;
    }

    private ArmedService createServiceBelgianArmy(Date startDate, Date endDate) throws PWCGException
    {
        ArmedService belgianAF = new ArmedService();
        belgianAF.setServiceId(AVIATION_MILITAIRE_BELGE);
        belgianAF.setCountry(new FCCountry(FCCountry.BELGIUM_CODE));
        belgianAF.setName(AVIATION_MILITAIRE_BELGE_NAME);
        belgianAF.setServiceIcon(AVIATION_MILITAIRE_BELGE_ICON);
        belgianAF.setStartDate(startDate);
        belgianAF.setEndDate(endDate);
        belgianAF.setServiceColorMap(new BelgianColorMap());
        belgianAF.setGeneralRankForService("General de Brigade");
        belgianAF.setDailyPersonnelReplacementRate(1);
        belgianAF.setDailyEquipmentReplacementRate(2);

        List<String> belgianPics = new ArrayList<String>();
        belgianPics.add("Belgian");
        belgianAF.setPicDirs(belgianPics);

        belgianAF.addServiceQuality(DateUtils.getBeginningOfWar(), 40);
        
        return belgianAF;
    }

    private ArmedService createServiceFrenchArmy(Date startDate, Date endDate) throws PWCGException
    {
        ArmedService frenchArmy = new ArmedService();
        frenchArmy.setServiceId(LAVIATION_MILITAIRE);
        frenchArmy.setCountry(new FCCountry(FCCountry.FRANCE_CODE));
        frenchArmy.setName(LAVIATION_MILITAIRE_NAME);
        frenchArmy.setServiceIcon(LAVIATION_MILITAIRE_ICON);
        frenchArmy.setStartDate(startDate);
        frenchArmy.setEndDate(endDate);
        frenchArmy.setServiceColorMap(new FrenchColorMap());
        frenchArmy.setGeneralRankForService("General de Brigade");
        frenchArmy.setDailyPersonnelReplacementRate(15);
        frenchArmy.setDailyEquipmentReplacementRate(30);

        List<String> frenchArmyPics = new ArrayList<String>();
        frenchArmyPics.add("French");
        frenchArmy.setPicDirs(frenchArmyPics);

        frenchArmy.addServiceQuality(DateUtils.getBeginningOfWar(), 40);
        
        return frenchArmy;
    }

	@Override
    public ArmedService getArmedServiceById(int serviceId, Date campaignDate) throws PWCGException 
	{
		if (serviceId == RFC || serviceId == RNAS)
		{
			if (campaignDate.before(DateUtils.getRAFDate()))
			{
			}
			else
			{
				serviceId = RAF;
			}
		}
			
		for(List <ArmedService> serviceList : armedServicesByCountry.values())
		{
			for (ArmedService service : serviceList)
			{
				if (service.getServiceId() == serviceId)
				{
					return service;
				}
			}
		}
		
		throw new PWCGException ("No service found for id = " + serviceId);
	}

	@Override
    public ArmedService getArmedServiceByName(String serviceName, Date campaignDate) throws PWCGException 
	{
		if (serviceName.equals(RFC_NAME) || serviceName.equals(RNAS_NAME))
		{
			if (campaignDate.before(DateUtils.getRAFDate()))
			{
			}
			else
			{
				serviceName = RAF_NAME;
			}
		}
			
		for(List <ArmedService> serviceList : armedServicesByCountry.values())
		{
			for (ArmedService service : serviceList)
			{
				if (service.getName().equals(serviceName))
				{
					return service;
				}
			}
		}
		
        throw new PWCGException ("No service found for name = " + serviceName);
	}

    @Override
    public List<ArmedService> getAlliedServices(Date date) throws PWCGException
    {
        List<ArmedService> alliedServices = new ArrayList<ArmedService>();
        
        for (int countryCode : armedServicesByCountry.keySet())
        {
            if (CountryFactory.makeCountryByCode(countryCode).getSide() == Side.ALLIED)
            {
                List<ArmedService> servicesForCountry = armedServicesByCountry.get(countryCode);
                
                for (ArmedService service: servicesForCountry)
                {
                    // RFC and RNAS before transition
                    if (service.getServiceId() == FCServiceManager.RFC || service.getServiceId() == FCServiceManager.RNAS)
                    {
                        if (date.before(DateUtils.getRAFDate()))
                        {
                            alliedServices.add(service);
                        }
                    }
                    // RAF before transition
                    else if (service.getServiceId() == FCServiceManager.RAF)
                    {
                        if (!date.before(DateUtils.getRAFDate()))
                        {
                            alliedServices.add(service);
                        }
                    }
                    // All other allied just add
                    else
                    {
                        alliedServices.add(service);
                    }
                }
            }
        }
        
        return alliedServices;
    }

    @Override
    public ArmedService getPrimaryServiceForNation(Country country, Date date) throws PWCGException
    {
        if (country == Country.BELGIUM)
        {
            return(getArmedServiceById(AVIATION_MILITAIRE_BELGE, date));
        }
        else if (country == Country.GERMANY)
        {
            return(getArmedServiceById(DEUTSCHE_LUFTSTREITKRAFTE, date));
        }
        else if (country == Country.FRANCE)
        {
            return(getArmedServiceById(LAVIATION_MILITAIRE, date));
        }
        else if (country == Country.USA)
        {
            return(getArmedServiceById(USAS, date));
        }
        else if (country == Country.BRITAIN)
        {
            if (!date.before(DateUtils.getRAFDate()))
            {
                return(getArmedServiceById(RFC, date));
            }
            else
            {
                return(getArmedServiceById(RAF, date));
            }
        }
        
        throw new PWCGException("Unexpected country for getPrimaryServiceForNation " + country);
    }

}