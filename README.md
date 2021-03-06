# NOTE:
This repository has been deprecated, please visit the [PocketAndroid repository for the latest on Pocket Android client development.](https://github.com/pokt-network/pocket-android)

# Pocket Android SDK
Standard Android SDK for the development of Pocket Node Network plugins. By leveraging this SDK
you can easily build any Blockchain network client and connect to any Pocket Node that supports
the given blockchain. For more information about Pocket Node you can checkout the repo [here](https://github.com/pokt-network/pocket-node).

For an example plugin feel free to check out our Ethereum plugin here: [https://github.com/pokt-network/pocket-android-eth](https://github.com/pokt-network/pocket-android-eth)

# Installation
This project is hosted in Github and you can install it using [Jitpack](https://www.jitpack.io/).

First add the following to your root `build.gradle`:

```
    allprojects {
		repositories {
			maven { url 'https://www.jitpack.io' }
		}
	}
```

Add the following to your Gradle file `dependencies` closure:

`implementation 'com.github.poktnetwork:pocket-android-sdk:0.0.1'`

***Optional***

In the case of having errors installing the dependency with the above steps, try adding the following
to the `dependencies` closure:

`implementation 'com.android.support:support-core-utils'`

# Creating your Pocket Android Plugin
A Network plugin will allow the application to send `Transaction` and `Query` objects to any given Pocket Node
that supports the desired Network (e.g. Eth, Aion, RChain).

## Subnetwork considerations
A subnetwork in terms of a Pocket Node is any given parallel network for a decentralized system, for example
in the case of Ethereum, besides Mainnet (network id `1`), you have access to the Rinkeby testnet (network id `4`), 
the Ropsten testnet (network id `3`), and so on. Please stick to using the standard network id's for the Network 
you're implementing.

This is useful to allow users to hop between networks, or for establishing differences between your application's 
test environment and production environments.

## Implementing the `PocketPlugin` interface
Implementing a class that extends the `PocketPlugin` will give any application using it access to the following functions:

### Creating and Importing a Wallet

`public abstract @NotNull Wallet createWallet(@NotNull String subnetwork, Map<String, Object> data) throws CreateWalletException`

Allows any application to create a Wallet for the specified `subnetwork`. Throws `CreateWalletException` in case of errors.

`public abstract @NotNull Wallet importWallet(@NotNull String privateKey, @NotNull String subnetwork, String address, Map<String, Object> data) throws ImportWalletException;`

Allows any application to import an existing Wallet from an account of the given `subnetwork`. Throws `ImportWalletException` in case of errors.

### Creating a Transaction

`public abstract @NotNull Transaction createTransaction(@NotNull Wallet wallet, @NotNull String subnetwork, Map<String, Object> params) throws CreateTransactionException;`

Creates a `Transaction` object to write to the given network with the given parameters and `subnetwork`. Throws `CreateTransactionException` in case of errors.

## Creating a Query

`public abstract @NotNull Query createQuery(@NotNull String subnetwork, Map<String, Object> params, Map<String, Object> decoder) throws CreateQueryException;`

Creates a `Query` object to read from the given network with the given parameters and `subnetwork`. Throws `CreateQueryException` in case of errors.

## Plugin Description

`public abstract @NotNull String getNetwork();`

Returns the network code for the plugin (e.g. ETH, AION, RCHAIN).

`public abstract @NotNull List<String> getSubnetworks();`

Return the list of supported `subnetwork`s for this plugin (e.g. 1,3,4).

# Using a Pocket Android Plugin
Just import the `PocketPlugin` child class and call into any of the functions described above. In addition to that you can use
the functions below to send the created `Transaction` and `Query` objects to your configured Pocket Node, either synchronously or asynchronously.

## The Configuration object
The constructor for any given `PocketPlugin` requires a class implementing the `Configuration` interface, check the Examples section for an implementation example.

## Sending Queries and Transactions synchrnously
The `PocketPlugin` superclass contains the following functions to send Queries and Transactions to the given Pocket Node:

`public <Q extends Query> @NotNull Q executeQuery(@NotNull Q query) throws IOException, JSONException`

`public <T extends Transaction> @NotNull T sendTransaction(@NotNull T transaction) throws IOException, JSONException`

## Sending Queries and Transactions asynchronously
The `PocketPlugin` superclass contains the following functions to send Queries and Transactions asynchronously to the given Pocket Node
 (Note the callback parameters in each function):

`public <Q extends Query> void executeQueryAsync(@NotNull SendRequestCallback<Q> callback)`

`public <T extends Transaction> void sendTransactionAsync(@NotNull SendRequestCallback<T> callback)`

# Examples

## PocketTestPlugin
For reference implementation of a `PocketPlugin` you can take a look at the `PocketTestPlugin` [here](https://raw.githubusercontent.com/pokt-network/pocket-android-sdk/master/sdk/src/androidTest/java/network/pokt/pocketsdk/plugin/PocketTestPlugin.java)

## TestConfiguration
For reference implementation of a `Configuration` object you can take a look at the `TestConfiguration` [here](https://github.com/pokt-network/pocket-android-sdk/blob/master/sdk/src/androidTest/java/network/pokt/pocketsdk/plugin/TestConfiguration.java).
