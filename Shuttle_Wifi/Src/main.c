/**
  ******************************************************************************
  * File Name          : main.c
  * Description        : Main program body
  ******************************************************************************
  *
  * COPYRIGHT(c) 2016 STMicroelectronics
  *
  * Redistribution and use in source and binary forms, with or without modification,
  * are permitted provided that the following conditions are met:
  *   1. Redistributions of source code must retain the above copyright notice,
  *      this list of conditions and the following disclaimer.
  *   2. Redistributions in binary form must reproduce the above copyright notice,
  *      this list of conditions and the following disclaimer in the documentation
  *      and/or other materials provided with the distribution.
  *   3. Neither the name of STMicroelectronics nor the names of its contributors
  *      may be used to endorse or promote products derived from this software
  *      without specific prior written permission.
  *
  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
  * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
  * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  *
  ******************************************************************************
  */
/* Includes ------------------------------------------------------------------*/
#include "stm32f4xx_hal.h"

/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Private variables ---------------------------------------------------------*/
UART_HandleTypeDef huart1;
UART_HandleTypeDef huart2;
UART_HandleTypeDef huart6;

/* USER CODE BEGIN PV */
/* Private variables ---------------------------------------------------------*/
char print_msg_buff[512];
#define ycp_printf(arg)   { memset(print_msg_buff, 0x00, sizeof(print_msg_buff)); \
                        sprintf((char*)print_msg_buff,arg);   \
                        HAL_UART_Transmit(&huart1, (uint8_t*)print_msg_buff, strlen(print_msg_buff), 1000); }
/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_USART1_UART_Init(void);
static void MX_USART2_UART_Init(void);
static void MX_USART6_UART_Init(void);

/* USER CODE BEGIN PFP */
/* Private function prototypes -----------------------------------------------*/
uint8_t hex_to_ascii(uint8_t hex);
/* USER CODE END PFP */

/* USER CODE BEGIN 0 */
int incr=0;
double latts=0,lonss=0,latts2=0,lonss2=0;
int id = 1;
double ongoinglat[] = {39.9489128059441,39.9490465712920,39.9491396252928,39.9492792060566,39.9493838914425,39.9495118400300,39.94961652506,39.9497095782854,39.9497095782854,39.9496630516885,39.9496281567201,39.9495874459011,39.9495874459011,39.9495816300678,39.9495525508941,39.9495002083501,39.9494711291418,39.9494594974550,39.9494246023827,39.9498026313843,39.9498200788262,39.9498666053163,39.9498898685495,39.9498956843566,39.9498898685495,39.9498898685495,39.9498724211254,39.9498607895068,39.9498433420752,39.9497793681214,39.9497444732123,39.9497270257511,39.9496863149909,39.9496775912534,39.9496107092292,39.9496078013136,39.9495845379845,39.9495729063169}; 
double ongoinglon[] = {-76.73023307695985,-76.73030734062195,-76.73036098480225,-76.73043072223663,-76.73050582408905,-76.73057019710541,-76.73063457012177,-76.730677485466,-76.73077404499054,-76.7308384180069,-76.73095107078552,-76.73106908798218,-76.73106908798218,-76.73119783401489,-76.73126220703125,-76.73136949539185,-76.73150897026062,-76.73161625862122,-76.73171281814575,-76.7324960231781,-76.73258185386658,-76.73266768455505,-76.73276424407959,-76.73284471035004,-76.73296809196472,-76.7330539226532,-76.73312902450562,-76.73320412635803,-76.73328995704651,-76.7333596944809,-76.73347234725952,-76.73352599143982,-76.7336493730545,-76.73371106386185,-76.73385053873062,-76.73396319150925,-76.73408389091492,-76.73416703939438};
 char str[120],str2[120],lonss_s[10],latts_s[10],lonss_s_2[10],latts_s_2[10],id_s1[10];

uint8_t gps_string[100],gps_char,gps_start_str[6];
uint8_t GPS_START[] = {'$','G','P','G','G','A'};
uint8_t lat_inc,lon_inc;
int lattemp=0,lontemp=0,lattempdec=0;
double latitude=0,longitude=0;
/* USER CODE END 0 */

int main(void)
{

  /* USER CODE BEGIN 1 */

  /* USER CODE END 1 */

  /* MCU Configuration----------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* Configure the system clock */
  SystemClock_Config();

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_USART1_UART_Init();
  MX_USART2_UART_Init();
  MX_USART6_UART_Init();

  /* USER CODE BEGIN 2 */

  /* USER CODE END 2 */

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {
    HAL_UART_Receive(&huart6, &gps_char, 1, 1);
    if(gps_char == '\n'){
      HAL_UART_Receive(&huart6, &gps_char, 1, 1);
      if(gps_char == '$'){
        gps_string[0] = gps_char;
        HAL_UART_Receive(&huart6, &gps_char, 1, 1);
        if(gps_char == 'G'){
          gps_string[1] = gps_char;
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
        if(gps_char == 'P'){
          gps_string[2] = gps_char;
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
        if(gps_char == 'G'){
          gps_string[3] = gps_char;
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
        if(gps_char == 'G'){
          gps_string[4] = gps_char;
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
        if(gps_char == 'A'){
          gps_string[5] = gps_char;
          //HAL_UART_Receive(&huart6, &gps_string[6], 50, 100);
          while(gps_char != ','){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          gps_char = 0x00;
          //get rid of time
          while(gps_char != ','){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          gps_char = 0x00;
          //now grab lat
         /* lat_inc=0;
          while(gps_char != ','){
            HAL_UART_Receive(&huart6, &gps_char, 1, 1);
            gps_string[6+lat_inc] = gps_char;
            lat_inc++;
          }*/
          //grab first 2 of lat
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          gps_string[6] = gps_char;
          lattemp = hex_to_ascii(gps_char);
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          gps_string[7] = gps_char;
          latitude = (double)((lattemp<<4) + hex_to_ascii(gps_char));
          
          

          lattemp =0 ;
          //grab next 2 for calc
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          gps_string[8] = gps_char;
          lattemp = hex_to_ascii(gps_char);
          
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          gps_string[9] = gps_char;
          lattemp = (lattemp<<4) + hex_to_ascii(gps_char);
          //get rid of '.'
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          //grab decimal for calc
          lattempdec=0;
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          lattempdec = (lattempdec<<4) + hex_to_ascii(gps_char);
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          lattempdec = (lattempdec<<4) + hex_to_ascii(gps_char);
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          lattempdec = (lattempdec<<4) + hex_to_ascii(gps_char);
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          lattempdec = (lattempdec<<4) + hex_to_ascii(gps_char);
          
          latitude += ((lattempdec)/600000.00); 
          
          
          ////////////}
          
          /*
          gps_char = 0x00;
          //while(gps_char != ','){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          lon_inc=0;
          while(gps_char != ','){
            HAL_UART_Receive(&huart6, &gps_char, 1, 1);
            gps_string[6+lat_inc+lon_inc] = gps_char;
            lon_inc++;
          }*/
          //HAL_UART_Receive(&huart6, &gps_string[10], 10, 100);
          //while(gps_char != 'N' || gps_char != 'S' ){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          /*
          while(gps_char != ','){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          HAL_UART_Receive(&huart6, &gps_string[20], 10, 100);
          //while(gps_char != 'N' || gps_char != 'S' ){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          HAL_UART_Receive(&huart6, &gps_char, 1, 1);
          HAL_UART_Receive(&huart6, &gps_string[30], 17, 100);
          while(gps_char != ','){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          //while(gps_char != 'W' || gps_char != 'E' ){HAL_UART_Receive(&huart6, &gps_char, 1, 1);}
          */
          
          gps_char='P';
      
      /*if(gps_char == '$'){
        HAL_UART_Receive(&huart6, &gps_char, 1, 1);
        if(gps_char == 'G'){*/
          
        }
        }
        }
        }
        }
      }
             
      
    }
    
    /*
    latts=ongoinglat[incr];//= 39.949651;
    lonss=ongoinglon[incr];//= -76.7340356;
    latts2=ongoinglat[incr+5];//= 39.949651;
    lonss2=ongoinglon[incr+5];//= -76.7340356;
    
     snprintf(latts_s, 10, "%lf", latts);
     snprintf(lonss_s, 10, "%lf", lonss);
     snprintf(latts_s_2, 10, "%lf", latts2);
     snprintf(lonss_s_2, 10, "%lf", lonss2);
     latts_s[9] = 0x33;
     lonss_s[9] = 0x33;
     if(incr<32){incr++;}
     else{incr=0;}
     
      strcpy (str,"AT+S.HTTPPOST=192.168.0.145,/shuttletracker/shuttleTracker,id=1");
      strcat (str,"&mac=jdawg&latitude=");
      strcat (str,latts_s);
      strcat (str,"&longitude=");
      strcat (str,lonss_s);
      strcat (str,",8081\r\n");
      ycp_printf(str);
      
      strcpy (str2,"AT+S.HTTPPOST=192.168.0.145,/shuttletracker/shuttleTracker,id=2");
      strcat (str2,"&mac=jdawg2&latitude=");
      strcat (str2,latts_s_2);
      strcat (str2,"&longitude=");
      strcat (str2,lonss_s_2);
      strcat (str2,",8081\r\n");
      ycp_printf(str2);
      //str=" ";
    /*ycp_printf((char)latts);
    ycp_printf("&longitude=");
    ycp_printf((char)lonss);
    ycp_printf(",8081\r\n");*/
   // HAL_Delay(1000);
    
  /* USER CODE END WHILE */

  /* USER CODE BEGIN 3 */

  }
  /* USER CODE END 3 */

}

/** System Clock Configuration
*/
void SystemClock_Config(void)
{

  RCC_OscInitTypeDef RCC_OscInitStruct;
  RCC_ClkInitTypeDef RCC_ClkInitStruct;

  __PWR_CLK_ENABLE();

  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE2);

  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.HSICalibrationValue = 16;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSI;
  RCC_OscInitStruct.PLL.PLLM = 16;
  RCC_OscInitStruct.PLL.PLLN = 336;
  RCC_OscInitStruct.PLL.PLLP = RCC_PLLP_DIV4;
  RCC_OscInitStruct.PLL.PLLQ = 7;
  HAL_RCC_OscConfig(&RCC_OscInitStruct);

  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV2;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;
  HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_2);

  HAL_SYSTICK_Config(HAL_RCC_GetHCLKFreq()/1000);

  HAL_SYSTICK_CLKSourceConfig(SYSTICK_CLKSOURCE_HCLK);

  /* SysTick_IRQn interrupt configuration */
  HAL_NVIC_SetPriority(SysTick_IRQn, 0, 0);
}

/* USART1 init function */
void MX_USART1_UART_Init(void)
{

  huart1.Instance = USART1;
  huart1.Init.BaudRate = 115200;
  huart1.Init.WordLength = UART_WORDLENGTH_8B;
  huart1.Init.StopBits = UART_STOPBITS_1;
  huart1.Init.Parity = UART_PARITY_NONE;
  huart1.Init.Mode = UART_MODE_TX_RX;
  huart1.Init.HwFlowCtl = UART_HWCONTROL_RTS_CTS;
  huart1.Init.OverSampling = UART_OVERSAMPLING_16;
  HAL_UART_Init(&huart1);

}

/* USART2 init function */
void MX_USART2_UART_Init(void)
{

  huart2.Instance = USART2;
  huart2.Init.BaudRate = 9600;
  huart2.Init.WordLength = UART_WORDLENGTH_9B;
  huart2.Init.StopBits = UART_STOPBITS_1;
  huart2.Init.Parity = UART_PARITY_NONE;
  huart2.Init.Mode = UART_MODE_RX;
  huart2.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart2.Init.OverSampling = UART_OVERSAMPLING_16;
  HAL_UART_Init(&huart2);

}

/* USART6 init function */
void MX_USART6_UART_Init(void)
{

  huart6.Instance = USART6;
  huart6.Init.BaudRate = 9600;
  huart6.Init.WordLength = UART_WORDLENGTH_8B;
  huart6.Init.StopBits = UART_STOPBITS_1;
  huart6.Init.Parity = UART_PARITY_NONE;
  huart6.Init.Mode = UART_MODE_RX;
  huart6.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart6.Init.OverSampling = UART_OVERSAMPLING_16;
  HAL_UART_Init(&huart6);

}

/** Configure pins as 
        * Analog 
        * Input 
        * Output
        * EVENT_OUT
        * EXTI
*/
void MX_GPIO_Init(void)
{

  GPIO_InitTypeDef GPIO_InitStruct;

  /* GPIO Ports Clock Enable */
  __GPIOC_CLK_ENABLE();
  __GPIOH_CLK_ENABLE();
  __GPIOA_CLK_ENABLE();
  __GPIOB_CLK_ENABLE();

  /*Configure GPIO pin : B1_Pin */
  GPIO_InitStruct.Pin = B1_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_EVT_RISING;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  HAL_GPIO_Init(B1_GPIO_Port, &GPIO_InitStruct);

  /*Configure GPIO pin : LD2_Pin */
  GPIO_InitStruct.Pin = LD2_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_LOW;
  HAL_GPIO_Init(LD2_GPIO_Port, &GPIO_InitStruct);

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(LD2_GPIO_Port, LD2_Pin, GPIO_PIN_RESET);

}

/* USER CODE BEGIN 4 */
uint8_t hex_to_ascii(uint8_t hex){
  if( hex ==  0x30){return    0;}
  if( hex ==  0x31){return    1;}
  if( hex ==  0x32){return    2;}
  if( hex ==  0x33){return    3;}
  if( hex ==  0x34){return    4;} 
  if( hex ==  0x35){return    5;}
  if( hex ==  0x36){return    6;}
  if( hex ==  0x37){return    7;}
  if( hex ==  0x38){return    8;}
  if( hex ==  0x39){return    9;}
  else{return 0;}
}
/* USER CODE END 4 */

#ifdef USE_FULL_ASSERT

/**
   * @brief Reports the name of the source file and the source line number
   * where the assert_param error has occurred.
   * @param file: pointer to the source file name
   * @param line: assert_param error line source number
   * @retval None
   */
void assert_failed(uint8_t* file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
    ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */

}

#endif

/**
  * @}
  */ 

/**
  * @}
*/ 

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
