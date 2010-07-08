/*
 * User: anna
 * Date: 06-Mar-2008
 */
package com.jetbrains.python;

import com.intellij.codeInsight.lookup.LookupElement;
import com.jetbrains.python.fixtures.PyLightFixtureTestCase;

import java.util.Arrays;

public class PythonCompletionTest extends PyLightFixtureTestCase {

  private void doTest() throws Exception {
    final String testName = "completion/" + getTestName(true);
    myFixture.configureByFile(testName + ".py");
    myFixture.completeBasic();
    myFixture.checkResultByFile(testName + ".after.py");
  }

  public void testLocalVar() throws Exception {
    doTest();
  }

  public void testSelfMethod() throws Exception {
    doTest();
  }

  public void testSelfField() throws Exception {
    doTest();
  }

  public void testFuncParams() throws Exception {
    doTest();
  }

  public void testFuncParamsStar() throws Exception {
    doTest();
  }

  public void testInitParams() throws Exception {
    doTest();
  }

  public void testSuperInitParams() throws Exception {      // PY-505
    doTest();
  }

  public void testSuperInitKwParams() throws Exception {      // PY-778
    doTest();
  }

  public void testPredefinedMethodName() throws Exception {
    doTest();
  }

  public void testPredefinedMethodNot() throws Exception {
    doTest();
  }

  public void testKeywordAfterComment() throws Exception {  // PY-697
    doTest();
  }

  public void testClassPrivate() throws Exception {
    doTest();
  }

  public void testClassPrivateNotInherited() throws Exception {
    doTest();
  }

  public void testClassPrivateNotPublic() throws Exception {
    doTest();
  }

  public void testTwoUnderscores() throws Exception {
    doTest();
  }

  public void testOneUnderscore() throws Exception {
    doTest();
  }

  public void testTwoUnderscoresNotOne() throws Exception {
    doTest();
  }

  public void testKwParamsInCodeUsage() throws Exception { //PY-1002
    doTest();
  }
  
  public void testKwParamsInCodeGetUsage() throws Exception { //PY-1002 
    doTest();
  }

  public void testSuperInitKwParamsNotOnlySelfAndKwArgs() throws Exception { //PY-1050 
    doTest();
  }

  public void testSuperInitKwParamsNoCompletion() throws Exception {
    doTest();
  }

  public void testIsInstance() throws Exception {
    doTest();    
  }

  public void testIsInstanceAssert() throws Exception {
    doTest();
  }

  public void testIsInstanceTuple() throws Exception {
    doTest();
  }

  public void testPropertyParens() throws Exception {  // PY-1037
    doTest();
  }

  public void testClassNameFromVarName() throws Exception {
    doTest();
  }

  public void testPropertyType() throws Exception {
    doTest();
  }

  public void testSeenMembers() throws Exception {  // PY-1181
    final String testName = "completion/" + getTestName(true);
    myFixture.configureByFile(testName + ".py");
    final LookupElement[] elements = myFixture.completeBasic();
    assertEquals(1, elements.length);
    assertEquals("children", elements [0].getLookupString());
  }

  public void testImportModule() throws Exception {
    final String testName = "completion/" + getTestName(true);
    myFixture.configureByFiles(testName + ".py", "completion/someModule.py");
    myFixture.completeBasic();
    myFixture.checkResultByFile(testName + ".after.py");
  }

  public void testPy255() throws Exception {
    final String dirname = "completion/";
    final String testName = dirname + "moduleClass";
    myFixture.configureByFiles(testName + ".py", dirname + "__init__.py");
    myFixture.copyDirectoryToProject(dirname + "mymodule", dirname + "mymodule");
    myFixture.completeBasic();
    myFixture.checkResultByFile(testName + ".after.py");
  }

  public void testPy874() throws Exception {
    final String dirname = "completion/";
    final String testName = dirname + "py874";
    myFixture.configureByFile(testName + ".py");
    myFixture.copyDirectoryToProject(dirname + "root", dirname + "root");
    myFixture.completeBasic();
    myFixture.checkResultByFile(testName + ".after.py");
  }

  public void testClassMethod() throws Exception {  // PY-833
    doTest();
  }

  public void testStarImport() throws Exception {
    myFixture.configureByFiles("completion/starImport/starImport.py", "completion/starImport/importSource.py");
    myFixture.completeBasic();
    assertSameElements(myFixture.getLookupElementStrings(), Arrays.asList("my_foo", "my_bar"));
  }

  public void testSlots() throws Exception {  // PY-1211
    doTest();
  }

  public void testReturnType() throws Exception {  
    doTest();
  }
}