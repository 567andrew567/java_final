# java_final

滾木塊遊戲，這是一個1*1*2的木塊，想辦法把它滾到終點吧
## 遊玩方式

上下左右鍵操作，R重新開始
* 綠色:起點
* 白色:路徑
* 黃色:終點
* 黑色:牆
* 灰色:洞

## 生地圖說明

讓方塊隨機走生路徑，把不是路徑的區域設成洞或是牆

* 隨機走方式
  * 隨便上下左右走，走過先設成牆
  * 用一個array紀錄路徑
  * 用Block自動判定可不可以走
  * 如果隨機走完還是留在原地就重新隨機這次
  * 避免卡死，如果重複10次都在同一個地方就全部重算
  * 把路徑換成路

* 牆、洞分辨方式，用dfs算相鄰的不是路的格子數
  * <50格設成洞
  * \>=50格設成牆
