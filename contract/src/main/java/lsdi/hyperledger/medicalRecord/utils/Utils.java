package lsdi.hyperledger.medicalRecord.utils;

import lsdi.hyperledger.medicalRecord.exceptions.AssetException;
import lsdi.hyperledger.medicalRecord.exceptions.CertificateException;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public static boolean AssetExists(final Context ctx, final String assetID) {
        String assetJSON = ctx.getStub().getStringState(assetID);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    private static String getClientId (Context ctx) {
        String clientCertificate = ctx.getClientIdentity().getId();

        Pattern pattern = Pattern.compile("CN=([^,\\s]+)");
        Matcher matcher = pattern.matcher(clientCertificate);

        if (matcher.find()) {
            return matcher.group(1); // Retorna o CN id do certificado
        }

        throw new ChaincodeException(CertificateException.CertificateErrors.ID_NOT_FOUND.name());
    }

    public static boolean hasClientSameId(Context ctx, String id) {
        var clientId = getClientId(ctx);

        if (clientId.equals(id)) {
            return true;
        }

        throw new ChaincodeException(AssetException.AssetTransferErrors.ACCESS_DENIED.name());
    }
}
