package lsdi.hyperledger.medicalRecord.contracts;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Transaction;

public class Utils {
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public static boolean AssetExists(final Context ctx, final String assetID) {
        String assetJSON = ctx.getStub().getStringState(assetID);

        return (assetJSON != null && !assetJSON.isEmpty());
    }
}
