package com.birschl.bitcoinbf.addressimport;

import com.birschl.bitcoinbf.addressimportOLD.ImportConfig;
import org.bitcoinj.core.*;
import org.bitcoinj.script.Script;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class TransactionProcessor {

    public static Stream<String> process(Transaction tx) {

        Set<String> addresses = new HashSet<>();
        for (TransactionOutput out : tx.getOutputs()) {
            Address addr = getOutputAddress(out);
            if (addr != null)
                addresses.add(addr.toString());
        }
        for (TransactionInput input : tx.getInputs()) {
            Address addr = getInputAddress(input);
            if (addr != null)
                addresses.add(addr.toString());
        }
        return addresses.stream();
    }

    private static Address getInputAddress(TransactionInput in) {
        try {
            if (!in.isCoinBase()) {
                return in.getFromAddress();
                //addr = in.getFromAddress().toBase58();
                // System.out.println(" <--- IN: "+addr);
            }
        } catch (ScriptException e) {

            // LOG.error("Error while reading transaction input address", e);
        }
        return null;
    }

    private static Address getOutputAddress(TransactionOutput out) {
        try {
            Script script = out.getScriptPubKey();
            if (script.isSentToAddress() || script.isPayToScriptHash()) {
                return script.getToAddress(ImportConfig.NETWORK_PARAMS);
            }
        } catch (ScriptException e) {
            // LOG.error("Error while reading transaction output address", e);
        }
        return null;
    }
}
