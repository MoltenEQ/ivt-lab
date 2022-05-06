package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import javax.net.ssl.TrustManager;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore primaryTorpedoStoreMock;
  private TorpedoStore secondaryTorpedoStoreMock;


  @BeforeEach
  public void init(){
    primaryTorpedoStoreMock =  mock(TorpedoStore.class);
    secondaryTorpedoStoreMock =  mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTorpedoStoreMock,secondaryTorpedoStoreMock);
  }

  @Test
  public void fireTorpedo_Single_BothFull_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStoreMock,times(1)).fire(1);
    verify(secondaryTorpedoStoreMock,times(0)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_PrimaryFull_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStoreMock,times(1)).fire(1);
    verify(secondaryTorpedoStoreMock,times(0)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_SecondaryFull_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStoreMock,times(0)).fire(1);
    verify(secondaryTorpedoStoreMock,times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_Empty_Fail(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);

    verify(primaryTorpedoStoreMock,times(0)).fire(1);
    verify(secondaryTorpedoStoreMock,times(0)).fire(1);

  }

  @Test 
  public void fireTorpedo_Single_Primary_Twice_Success(){

    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(false);

    // Act
    boolean firstFireResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondFireResult = ship.fireTorpedo(FiringMode.SINGLE);


    // Assert
    assertEquals(true, firstFireResult);
    assertEquals(true, secondFireResult);

    verify(primaryTorpedoStoreMock,times(2)).fire(1);
    verify(secondaryTorpedoStoreMock,times(0)).fire(1);
  } 


  @Test
  public void fireTorpedo_Single_Complex_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);
    
    boolean setupFireResult = ship.fireTorpedo(FiringMode.SINGLE); // WE forced secondary to FIRE, so it was last

    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean firstFireResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondFireResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean thirdFireResult = ship.fireTorpedo(FiringMode.SINGLE);


    // Assert
    assertEquals(true,setupFireResult);
    assertEquals(true, firstFireResult);
    assertEquals(true, secondFireResult);
    assertEquals(true, thirdFireResult);


    verify(primaryTorpedoStoreMock,times(2)).fire(1);
    verify(secondaryTorpedoStoreMock,times(2)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_Empty_Complex(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(false);
    
    boolean setupFireResult = ship.fireTorpedo(FiringMode.SINGLE); // WE forced primary to FIRE, so it was last

    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    // Assert
    assertEquals(true,setupFireResult);
    assertEquals(false, result);

    verify(primaryTorpedoStoreMock,times(1)).fire(1);
    verify(secondaryTorpedoStoreMock,times(0)).fire(1);

  }

  @Test
  public void fireTorpedo_All_BothFull_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStoreMock,times(1)).fire(1);
    verify(secondaryTorpedoStoreMock,times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_PrimaryFull_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStoreMock,times(1)).fire(1);
    verify(secondaryTorpedoStoreMock,times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_SecondaryFull_Success(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(false);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(false);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStoreMock,times(0)).fire(1);
    verify(secondaryTorpedoStoreMock,times(1)).fire(1);
  }

    @Test
  public void fireTorpedo_All_BothEmpty_Fail(){
    // Arrange
    when(primaryTorpedoStoreMock.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStoreMock.isEmpty()).thenReturn(true);

    when(primaryTorpedoStoreMock.fire(1)).thenReturn(true);
    when(secondaryTorpedoStoreMock.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);

    verify(primaryTorpedoStoreMock,times(0)).fire(1);
    verify(secondaryTorpedoStoreMock,times(0)).fire(1);
  }

  @Test
  public void fireLaser_Not_Implemented_Fail(){
    // Arrange


    // Act
    boolean result = ship.fireLaser(FiringMode.ALL);

    // Assert
    assertEquals(false, result);


  }
}
