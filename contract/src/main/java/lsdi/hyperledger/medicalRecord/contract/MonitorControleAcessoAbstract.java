package lsdi.hyperledger.medicalRecord.contract;

import java.util.Arrays;

import com.owlike.genson.Genson;

import lsdi.hyperledger.medicalRecord.exceptions.AssetException;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;

import static lsdi.hyperledger.medicalRecord.utils.Utils.hasClientSameId;

public abstract class MonitorControleAcessoAbstract {
    protected final Genson genson;
    
    protected MonitorControleAcessoAbstract(Genson genson) {
        this.genson = genson;
    }

    private String[] obterPapeis(Context ctx) {
        return (ctx.getClientIdentity().getAttributeValue("role")).split(";");
    }

    public boolean usuarioTemPapel(Context ctx, String papel) {
        String[] papeis = obterPapeis(ctx);
        if (Arrays.asList(papeis).contains(papel)) {
            return true;
        }

        throw new ChaincodeException(AssetException.AssetTransferErrors.ACCESS_DENIED.name());
    }

    public boolean temAcessoComRestricaoPorIdUsuario(Context ctx, String id, String [] papeisComTotalAcesso, String[] papeisAcessoRestricaoPorIdUsuario) {
        String[] papeis = obterPapeis(ctx);

        // Verifica se a identidade contém um papel com total acesso
        for (String role : papeis) {
            if (Arrays.asList(papeisComTotalAcesso).contains(role)) {
                return true;
            }
        }

        // Verifica se a identidade contém um papel com restrição de acesso
        for (String role : papeis) {
            if (Arrays.asList(papeisAcessoRestricaoPorIdUsuario).contains(role)) {
                return hasClientSameId(ctx, id);
            }
        }
        throw new ChaincodeException(AssetException.AssetTransferErrors.ACCESS_DENIED.name());
    }
}
