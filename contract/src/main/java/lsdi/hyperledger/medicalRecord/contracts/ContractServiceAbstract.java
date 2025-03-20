package lsdi.hyperledger.medicalRecord.contracts;

import com.owlike.genson.Genson;

public abstract class ContractServiceAbstract {
    protected final Genson genson;
    
    ContractServiceAbstract(Genson genson) {
        this.genson = genson;
    }
}
