package fr.ensma.lias.bimedia2018machinelearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.bimedia2018machinelearning.preprocessing.controlers.TicketControl;

public class AssociationPrep {
	public static void main( String[] args ) 
    {
		List<Integer> pdvs= new ArrayList<Integer>();
		pdvs.add(350459);
		//pdvs.add(20369);
		/*pdvs.add(22740);
		pdvs.add(29009);
		pdvs.add(29015);
		pdvs.add(29022);
		pdvs.add(30415);
		pdvs.add(59008);
		pdvs.add(62737);
		pdvs.add(69001);
		pdvs.add(100148);
		pdvs.add(130873);
		pdvs.add(149018);
		pdvs.add(169017);
		pdvs.add(179018);
		pdvs.add(180049);
		pdvs.add(180073);
		pdvs.add(189004);
		pdvs.add(189013);
		pdvs.add(200145);
		pdvs.add(240155);
		pdvs.add(259003);
		pdvs.add(259011);
		pdvs.add(260081);
		pdvs.add(260120);
		pdvs.add(260211);
		pdvs.add(260526);
		pdvs.add(279022);
		pdvs.add(279028);
		pdvs.add(279031);
		pdvs.add(280103);
		pdvs.add(280193);
		pdvs.add(290201);
		pdvs.add(299049);
		pdvs.add(299077);
		pdvs.add(299088);
		pdvs.add(310414);
		pdvs.add(310699);
		pdvs.add(330159);
		pdvs.add(340223);
		pdvs.add(340225);
		pdvs.add(340861);
		pdvs.add(350056);
		pdvs.add(359003);
		pdvs.add(370014);
		pdvs.add(370921);
		pdvs.add(420458);
		pdvs.add(439008);
		pdvs.add(440062);
		pdvs.add(440282);
		pdvs.add(440414);
		pdvs.add(440597);
		pdvs.add(450335);
		pdvs.add(450948);
		pdvs.add(470268);
		pdvs.add(480046);
		pdvs.add(490414);
		pdvs.add(560219);
		pdvs.add(561320);
		pdvs.add(579005);
		pdvs.add(590857);
		pdvs.add(599019);
		pdvs.add(600247);
		pdvs.add(609005);
		pdvs.add(620215);
		pdvs.add(621147);
		pdvs.add(629022);
		pdvs.add(629024);
		pdvs.add(640071);
		pdvs.add(650135);
		pdvs.add(670447);
		pdvs.add(680059);
		pdvs.add(689008);
		pdvs.add(699011);
		pdvs.add(700109);
		pdvs.add(740009);
		pdvs.add(749002);
		pdvs.add(759039);
		pdvs.add(780464);
		pdvs.add(790171);
		pdvs.add(809027);
		pdvs.add(810307);
		pdvs.add(830014);
		pdvs.add(840232);
		pdvs.add(859013);
		pdvs.add(860540);
		pdvs.add(880495);
		pdvs.add(899021);
		pdvs.add(910474);
		pdvs.add(920138);
		pdvs.add(920204);
		pdvs.add(920839);
		pdvs.add(939013);
		pdvs.add(940132);
		pdvs.add(940267);
		pdvs.add(940279);*/
		 for (int pdv : pdvs) {
		TicketControl control = new TicketControl();
		try {
			control.fusion("C:\\Users\\ASUS\\Desktop\\ditinctProduct\\data"+pdv+".csv", "C:\\Users\\ASUS\\Desktop\\ditinctProduct\\data\\data"+pdv+".txt", ';');
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  	
		}
    }
}