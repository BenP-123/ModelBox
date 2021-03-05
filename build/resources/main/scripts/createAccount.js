
/* *
*
*   Issue seems to be with these two lines, probably the import
*   If they are both commented out the function debugging section executes fine.
*
*   Issue:  Figure out why the import statement isn't working with GraalVM
*           If this javascript file is executed as standalone it works properly and creates a
*           new user. If we can figure this out we can add a lot of new features using JavaScript!
*  */
//import * as Realm from "realm-web";
//const app = new Realm.App({ id: "modelbox-vqzyc" });

async function registerAccount(email, password) {

    //Debugging
    console.log("Hey from createAccount.js")
    console.log(email)
    console.log(password)
    //End Debugging, everything was passed properly via parameter.

    //Create Account
    //await app.emailPasswordAuth.registerUser(email, password)

    /* As of now users are approved automatically without confirmation for Testing Purposes */
    //await app.emailPasswordAuth.resendConfirmationEmail(email)

}

/* Using this Standalone works! Run -> createAccount.js */
//registerAccount("testemail@testmail.com", "modelbox").then(r => console.log("Geeee"));
