package iso.caixas.mp4;

public class DecritorObjetoInicial extends DescritorObjeto{
   public int iddescritorobjeto; //10 bit
   byte bandeiraUrl;
   byte include;
   byte reservado;
   
   //flag 1
   byte URLlength;
   byte URLstring[] = new byte[URLlength];
   //flag 0
   int ODProfileLevelIndication;
   int sceneProfileLevelIndication;
   int audioProfileLevelIndication;
   int visualProfileLevelIndication;
   int graphicsProfileLevelIndication;
  // ES_Descriptor esDescr[1 .. 255];
  // OCI_Descriptor ociDescr[0 .. 255];
 // IPMP_DescriptorPointer ipmpDescrPtr[0 .. 255];
 //  IPMP_Descriptor ipmpDescr [0 .. 255];
  // IPMP_ToolListDescriptor toolListDescr[0 .. 1];
   
   
}
