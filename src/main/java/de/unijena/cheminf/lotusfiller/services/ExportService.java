package de.unijena.cheminf.lotusfiller.services;

import de.unijena.cheminf.lotusfiller.mongocollections.LotusUniqueNaturalProduct;
import de.unijena.cheminf.lotusfiller.mongocollections.LotusUniqueNaturalProductRepository;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


@Service
public class ExportService {

    @Autowired
    LotusUniqueNaturalProductRepository lotusUniqueNaturalProductRepository;

    @Autowired
    AtomContainerToUniqueNaturalProductService atomContainerToUniqueNaturalProductService;


    public void generateTSV(String filename){
        System.out.println("Generating TSV file for all LOTUS");

        FileWriter fw = null;
        try {
            fw = new FileWriter(filename);

            List<LotusUniqueNaturalProduct> allNP = lotusUniqueNaturalProductRepository.findAll();
            int count = 0;
            for(LotusUniqueNaturalProduct np : allNP){
                fw.write(np.lotus_id+"\t"+np.smiles+"\n");
            }

            fw.close();
            System.out.println("Successfully wrote to the file.");


        } catch (IOException e) {
            e.printStackTrace();
        }




        System.out.println("done");

    }


    public void generateSDF(String filename){
        System.out.println("Generating SDF file for all LOTUS");



        FileWriter fw = null;
        try {
            fw = new FileWriter(filename);

            SDFWriter writer = new SDFWriter(fw);

            //List<LotusUniqueNaturalProduct> allNP = lotusUniqueNaturalProductRepository.findAll();

            List<String> lotus_ids = lotusUniqueNaturalProductRepository.findAllLotusIds();
            int count = 0;

            for(String lotus_id : lotus_ids){
                try {
                    LotusUniqueNaturalProduct np = lotusUniqueNaturalProductRepository.findByLotus_id(lotus_id).get(0);
                    IAtomContainer ac = atomContainerToUniqueNaturalProductService.createAtomContainer(np);

                    // add most of molecular descriptors and available metadata
                    ac.setProperty("coconut_id", np.lotus_id);
                    ac.setProperty("inchi", np.inchi);
                    ac.setProperty("inchikey", np.inchikey);
                    ac.setProperty("SMILES", np.smiles);
                    ac.setProperty("sugar_free_smiles", np.sugar_free_smiles);
                    ac.setProperty("molecular_formula", np.molecular_formula);
                    ac.setProperty("molecular_weight", np.molecular_weight);
                    ac.setProperty("citationDOI", np.taxonomyReferenceObjects.keySet().toArray().toString());
                    ac.setProperty("textTaxa", np.taxonomyReferenceObjects.toString());
                    ac.setProperty("name", np.traditional_name);
                    ac.setProperty("synonyms", np.synonyms.toString());
                    ac.setProperty("NPL score", np.npl_score);
                    ac.setProperty("number_of_carbons", np.number_of_carbons);
                    ac.setProperty("number_of_nitrogens", np.number_of_nitrogens);
                    ac.setProperty("number_of_oxygens", np.number_of_oxygens);
                    ac.setProperty("number_of_rings", np.number_of_rings);
                    ac.setProperty("total_atom_number", np.total_atom_number);
                    ac.setProperty("bond_count", np.bond_count);
                    ac.setProperty("murko_framework", np.murko_framework);
                    ac.setProperty("alogp", np.alogp);
                    ac.setProperty("apol", np.apol);
                    ac.setProperty("topoPSA", np.topoPSA);

                    writer.write(ac);

                    count++;

                    if (count % 50000 == 0) {
                        System.out.println("Molecules written: " + count);
                    }
                }catch(ConversionFailedException e){
                    System.out.println(lotus_id);
                }


            }

            writer.close();

        } catch (IOException | CDKException e) {
            e.printStackTrace();
        }




    System.out.println("done");

    }
}
