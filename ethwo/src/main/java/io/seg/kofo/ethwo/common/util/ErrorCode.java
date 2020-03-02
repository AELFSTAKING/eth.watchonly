package io.seg.kofo.ethwo.common.util;


/**
 * @author WalkerLiu
 * @date 2018/10/15
 */
public class ErrorCode {
    /*------------------------------------validate tx failed error -----------------------------------------------------------------------------*/
    //public static final Long InvalidSender is returned if the transaction contains an invalid signature.
    @ErrorTemplate(msg = "invalid sender")
    public static final Long InvalidSender = -32001L;

    //public static final Long NonceTooLow is returned if the nonce of a transaction is lower than the
    // one present in the local chain.
    @ErrorTemplate(msg = "nonce too low")
    public static final Long NonceTooLow = -32002L;

    //public static final Long Underpriced is returned if a transaction's gas price is below the minimum
    // configured for the transaction pool.
    @ErrorTemplate(msg = "transaction underpriced")
    public static final Long Underpriced = -32003L;

    //public static final Long ReplaceUnderpriced is returned if a transaction is attempted to be replaced
    // with a different one without the required price bump.
    @ErrorTemplate(msg = "replacement transaction underpriced")
    public static final Long ReplaceUnderpriced = -32004L;

    //public static final Long InsufficientFunds is returned if the total cost of executing a transaction
    // is higher than the balance of the user's account.
    @ErrorTemplate(msg = "insufficient funds for gas * price + value")
    public static final Long InsufficientFunds = -32005L;

    //public static final Long IntrinsicGas is returned if the transaction is specified to use less gas
    // than required to start the invocation.
    @ErrorTemplate(msg = "intrinsic gas too low")
    public static final Long IntrinsicGas = -32006L;

    //public static final Long GasLimit is returned if a transaction's requested gas limit exceeds the
    // maximum allowance of the current block.
    @ErrorTemplate(msg = "exceeds block gas limit")
    public static final Long GasLimit = -32007L;

    //public static final Long NegativeValue is a sanitypublic static final Long or to ensure noone is able to specify a
    // transaction with a negative value.
    @ErrorTemplate(msg = "negative value")
    public static final Long NegativeValue = -32008L;

    //public static final Long OversizedData is returned if the input data of a transaction is greater
    // than some meaningful limit a user might use. This is not a consensuspublic static final Long or
    // making the transaction invalid, rather a DOS protection.
    @ErrorTemplate(msg = "oversized data")
    public static final Long OversizedData = -32009L;

    @ErrorTemplate(msg = "invalid transaction v, r, s values")
    public static final Long InvalidSig = -32010L;

    // public static final Long UnknownAncestor is returned when validating a block requires an ancestor
    // that is unknown.
    @ErrorTemplate(msg = "unknown ancestor")
    public static final Long UnknownAncestor = -32011L;

    // public static final Long InvalidNumber is returned if a block's number doesn't equal it's parent's
    // plus one.
    @ErrorTemplate(msg = "invalid block number")
    public static final Long InvalidNumber = -32012L;

    @ErrorTemplate(msg = "transaction not exist")
    public static final Long TxNotExist = -32013L;


    @ErrorTemplate(msg = "method not found")
    public static final Long MethodNotFound = -32014L;

    @ErrorTemplate(msg = "request error")
    public static final Long RequestError = -32015L;

    @ErrorTemplate(msg = "invalid message")
    public static final Long InvalidMessage = -32016L;

    @ErrorTemplate(msg = "parameter error")
    public static final Long ParameterError = -32017L;


}
