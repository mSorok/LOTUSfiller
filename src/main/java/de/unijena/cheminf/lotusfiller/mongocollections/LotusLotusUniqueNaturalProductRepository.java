package de.unijena.cheminf.lotusfiller.mongocollections;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LotusLotusUniqueNaturalProductRepository extends MongoRepository<LotusUniqueNaturalProduct, String>, LotusUniqueNaturalProductRepositoryCustom {

    public List<LotusUniqueNaturalProduct> findBySmiles(String smiles);

    public List<LotusUniqueNaturalProduct> findByInchi(String inchi);

    public List<LotusUniqueNaturalProduct> findByInchikey(String inchikey);

    @Query("{ coconut_id : ?0}")
    public List<LotusUniqueNaturalProduct> findByCoconut_id(String coconut_id);

    @Query("{ clean_smiles : ?0}")
    public List<LotusUniqueNaturalProduct> findByClean_smiles(String clean_smiles);

    @Query("{molecular_formula : ?0}")
    public List<LotusUniqueNaturalProduct> findByMolecular_formula(String molecular_formula);

    public List<LotusUniqueNaturalProduct> findByName(String name);

    @Query("{ $text: { $search: ?0 } }")
    public List<LotusUniqueNaturalProduct> fuzzyNameSearch(String name);




    @Query("{ npl_noh_score: { $exists:false } }")
    List<LotusUniqueNaturalProduct> findAllByNPLScoreComputed();

    @Query("{ apol: { $exists:false } }")
    List<LotusUniqueNaturalProduct> findAllByApolComputed();

    @Query("{ pubchemBits : { $bitsAllSet : ?0  }}")
    List<LotusUniqueNaturalProduct> findAllPubchemBitsSet(byte[] querybits) ;




}