import { Bell, Settings, ShoppingBag, QrCode, Trophy, Recycle, Award, Leaf, Medal, Flame, Shield, Check } from 'lucide-react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Avatar, AvatarFallback } from './ui/avatar';
import { Badge } from './ui/badge';
import { useState } from 'react';
import { QRScanner } from './QRScanner';
import { ScanResult } from './ScanResult';
import { Ranking } from './Ranking';
import { Achievements } from './Achievements';
import { Store } from './Store';
import { Notifications } from './Notifications';
import { Settings as SettingsPanel } from './Settings';
import { Profile } from './Profile';

interface MainMenuProps {
  userName: string;
  userEmail: string;
}

export function MainMenu({ userName, userEmail }: MainMenuProps) {
  const totalPoints = 1250;
  const userLevel = 5;
  const [showScanner, setShowScanner] = useState(false);
  const [showResult, setShowResult] = useState(false);
  const [showRanking, setShowRanking] = useState(false);
  const [showAchievements, setShowAchievements] = useState(false);
  const [showStore, setShowStore] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showSettings, setShowSettings] = useState(false);
  const [showProfile, setShowProfile] = useState(false);
  const [scannedData, setScannedData] = useState('');

  // Datos de racha
  const streakDays = 5;
  const protectedStreaks = 1;
  const weekDays = ['D', 'L', 'M', 'X', 'J', 'V', 'S'];
  const completedDays = [true, true, true, true, true, false, false]; // 5 días completados

  const handleScanSuccess = (data: string) => {
    setScannedData(data);
    setShowScanner(false);
    setShowResult(true);
  };

  const handleContinueAfterScan = () => {
    setShowResult(false);
    // Aquí podrías actualizar los puntos del usuario
  };

  const handleOpenSettings = () => {
    console.log('Abriendo configuración...');
    setShowSettings(true);
  };

  if (showScanner) {
    return (
      <QRScanner 
        onClose={() => setShowScanner(false)}
        onScanSuccess={handleScanSuccess}
      />
    );
  }

  if (showRanking) {
    return <Ranking userName={userName} onBack={() => setShowRanking(false)} />;
  }

  if (showAchievements) {
    return <Achievements userName={userName} onBack={() => setShowAchievements(false)} />;
  }

  if (showStore) {
    return <Store userName={userName} onBack={() => setShowStore(false)} />;
  }

  if (showSettings) {
    return <SettingsPanel userName={userName} userEmail={userEmail} onBack={() => setShowSettings(false)} />;
  }

  if (showProfile) {
    return <Profile userName={userName} userLevel={userLevel} totalPoints={totalPoints} streakDays={streakDays} onBack={() => setShowProfile(false)} />;
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-teal-50 to-cyan-50 pb-32">
      {/* Header con glassmorphism */}
      <div className="relative bg-gradient-to-r from-emerald-600 via-green-600 to-teal-600 pt-12 pb-8 px-6 rounded-b-[2.5rem] shadow-2xl overflow-hidden">
        {/* Efectos de fondo animados */}
        <div className="absolute inset-0 overflow-hidden">
          <div className="absolute top-0 right-0 w-64 h-64 bg-teal-400/30 rounded-full blur-3xl animate-float"></div>
          <div className="absolute bottom-0 left-0 w-48 h-48 bg-emerald-400/20 rounded-full blur-2xl animate-pulse-glow"></div>
          <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-green-400/10 rounded-full blur-3xl"></div>
        </div>

        <div className="relative z-10">
          <div className="flex items-center justify-between mb-6">
            {/* Avatar/Perfil mejorado */}
            <button 
              onClick={() => setShowProfile(true)}
              className="relative group"
            >
              <div className="absolute -inset-1 bg-gradient-to-r from-yellow-400 to-amber-500 rounded-full blur opacity-40 group-hover:opacity-70 transition duration-300"></div>
              <Avatar className="relative h-16 w-16 border-3 border-white shadow-2xl ring-4 ring-white/20">
                <AvatarFallback className="bg-gradient-to-br from-emerald-600 to-teal-700 text-white text-xl">
                  {userName.charAt(0).toUpperCase()}
                </AvatarFallback>
              </Avatar>
              <div className="absolute -bottom-1 -right-1 bg-gradient-to-br from-yellow-400 to-amber-500 rounded-full p-1.5 shadow-lg ring-2 ring-white">
                <Award className="w-4 h-4 text-white" />
              </div>
            </button>

            {/* Botones de acción mejorados */}
            <div className="flex gap-3">
              <button 
                onClick={() => setShowNotifications(true)}
                className="relative bg-white/20 backdrop-blur-md p-3.5 rounded-2xl hover:bg-white/30 transition-all shadow-lg border border-white/30 group"
              >
                <Bell className="w-6 h-6 text-white group-hover:scale-110 transition-transform" />
                <span className="absolute top-2 right-2 w-3 h-3 bg-gradient-to-r from-red-500 to-pink-500 rounded-full border-2 border-white animate-pulse"></span>
              </button>
              <button 
                onClick={handleOpenSettings}
                className="bg-white/20 backdrop-blur-md p-3.5 rounded-2xl hover:bg-white/30 transition-all shadow-lg border border-white/30 group"
              >
                <Settings className="w-6 h-6 text-white group-hover:rotate-90 transition-transform duration-500" />
              </button>
            </div>
          </div>

          {/* Info del usuario mejorada */}
          <div className="text-white">
            <h2 className="mb-2 drop-shadow-lg">¡Hola, {userName}!</h2>
            <div className="flex items-center gap-2">
              <Badge className="bg-white/25 backdrop-blur-sm text-white border-0 shadow-lg px-3 py-1">
                <Leaf className="w-4 h-4 mr-1.5" />
                Nivel {userLevel}
              </Badge>
            </div>
          </div>
        </div>
      </div>

      {/* Contenido principal */}
      <div className="px-6 -mt-8 pb-8 space-y-6 animate-slide-up">
        {/* Card de puntos mejorada */}
        <Card className="relative bg-white shadow-2xl border-0 rounded-3xl p-8 overflow-hidden group hover:shadow-3xl transition-all duration-300">
          {/* Decoración de fondo */}
          <div className="absolute top-0 right-0 w-32 h-32 bg-gradient-to-br from-green-100 to-emerald-100 rounded-full blur-3xl opacity-50"></div>
          
          <div className="relative text-center">
            <div className="inline-flex items-center justify-center w-28 h-28 bg-gradient-to-br from-emerald-400 via-green-500 to-teal-600 rounded-full mb-5 shadow-2xl relative overflow-hidden group-hover:scale-105 transition-transform duration-300">
              <div className="absolute inset-0 bg-white/20 animate-pulse"></div>
              <Recycle className="w-14 h-14 text-white animate-[spin_4s_linear_infinite] drop-shadow-lg" />
            </div>
            <p className="text-gray-600 mb-3">Puntaje Total</p>
            <div className="flex items-center justify-center gap-3">
              <h1 className="bg-gradient-to-r from-green-600 to-emerald-600 bg-clip-text text-transparent">{totalPoints.toLocaleString()}</h1>
              <span className="text-green-600">pts</span>
            </div>
            <div className="mt-6 pt-6 border-t border-gray-100">
              <div className="flex items-center justify-center gap-2 text-gray-600">
                <div className="bg-yellow-100 rounded-full p-2">
                  <Trophy className="w-5 h-5 text-yellow-600" />
                </div>
                <span>Top 15% de recicladores</span>
              </div>
            </div>
          </div>
        </Card>

        {/* Racha Timeline mejorada */}
        <Card className="relative bg-gradient-to-br from-orange-50 via-amber-50 to-yellow-50 border-0 shadow-2xl rounded-3xl overflow-hidden">
          {/* Decoración de fondo mejorada */}
          <div className="absolute top-0 right-0 w-40 h-40 bg-gradient-to-br from-orange-300/30 to-amber-300/20 rounded-full blur-3xl"></div>
          <div className="absolute bottom-0 left-0 w-32 h-32 bg-gradient-to-br from-yellow-300/20 to-orange-300/10 rounded-full blur-2xl"></div>
          
          <div className="p-6 relative">
            <div className="flex items-start justify-between mb-8">
              <div className="flex-1">
                <div className="flex items-center gap-4 mb-3">
                  <div className="relative">
                    <div className="absolute inset-0 bg-gradient-to-br from-orange-400 to-red-500 rounded-2xl blur opacity-50"></div>
                    <div className="relative bg-gradient-to-br from-orange-500 to-red-600 p-3 rounded-2xl shadow-xl">
                      <Flame className="w-7 h-7 text-white drop-shadow-lg" />
                    </div>
                  </div>
                  <div>
                    <h3 className="text-orange-900 mb-1">{streakDays} días consecutivos</h3>
                    <p className="text-orange-600">¡Mantén el ritmo!</p>
                  </div>
                </div>
              </div>
            </div>

            {/* Timeline de la semana mejorado */}
            <div className="mb-6">
              <div className="flex items-center justify-between">
                {weekDays.map((day, index) => {
                  const isToday = index === 4;
                  return (
                    <div key={index} className="flex flex-col items-center gap-3 relative">
                      <span className={`transition-all duration-300 ${
                        completedDays[index] ? 'text-green-700 font-semibold' : 'text-gray-400'
                      }`}>
                        {day}
                      </span>
                      <div 
                        className={`relative w-14 h-14 rounded-full flex items-center justify-center transition-all duration-500 ${
                          completedDays[index]
                            ? 'bg-gradient-to-br from-green-400 via-emerald-500 to-teal-600 shadow-xl scale-110 hover:scale-125'
                            : 'bg-white border-3 border-dashed border-gray-300 hover:border-green-300'
                        } ${isToday && completedDays[index] ? 'ring-4 ring-green-300/50 ring-offset-2 animate-pulse-glow' : ''}`}
                      >
                        {completedDays[index] && (
                          <>
                            <div className="absolute inset-0 bg-white/20 rounded-full animate-pulse"></div>
                            <Check className="relative w-7 h-7 text-white drop-shadow-lg" />
                          </>
                        )}
                        {!completedDays[index] && isToday && (
                          <div className="w-4 h-4 bg-gradient-to-r from-orange-400 to-red-500 rounded-full animate-pulse shadow-lg"></div>
                        )}
                      </div>
                      {completedDays[index] && (
                        <div className="absolute -top-1 -right-1 w-5 h-5 bg-gradient-to-br from-yellow-400 to-amber-500 rounded-full border-2 border-orange-50 shadow-md animate-pulse"></div>
                      )}
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Info adicional */}
            <div className="grid grid-cols-2 gap-3">
              {protectedStreaks > 0 && (
                <div className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-xl p-3 flex items-center gap-2 border border-blue-200/50 shadow-sm">
                  <div className="bg-blue-600 rounded-lg p-1.5">
                    <Shield className="w-4 h-4 text-white" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="text-blue-900">{protectedStreaks}</div>
                    <p className="text-blue-600 truncate">Protección</p>
                  </div>
                </div>
              )}
              <div className="bg-gradient-to-br from-amber-50 to-amber-100 rounded-xl p-3 flex items-center gap-2 border border-amber-200/50 shadow-sm">
                <div className="bg-amber-600 rounded-lg p-1.5">
                  <Trophy className="w-4 h-4 text-white" />
                </div>
                <div className="flex-1 min-w-0">
                  <div className="text-amber-900">{streakDays * 10}</div>
                  <p className="text-amber-600 truncate">Pts bonus</p>
                </div>
              </div>
            </div>
          </div>
        </Card>

        {/* Estadísticas rápidas */}
        <div className="grid grid-cols-2 gap-4 mb-8">
          <Card className="bg-gradient-to-br from-blue-50 to-blue-100 border-0 p-4 text-center shadow-md">
            <div className="text-blue-600 mb-1">24</div>
            <p className="text-gray-600">Reciclajes</p>
          </Card>
          <Card className="bg-gradient-to-br from-green-50 to-green-100 border-0 p-4 text-center shadow-md">
            <div className="text-green-600 mb-1">5.2kg</div>
            <p className="text-gray-600">Total</p>
          </Card>
        </div>

        {/* Acciones rápidas */}
        <div className="mb-8">
          <h3 className="text-gray-800 mb-4">Acción Rápida</h3>
          <Button 
            onClick={() => setShowAchievements(true)}
            className="w-full bg-gradient-to-r from-amber-500 to-yellow-600 hover:from-amber-600 hover:to-yellow-700 h-16 shadow-lg"
          >
            <Medal className="w-6 h-6 mr-3" />
            <span>Ver Mis Logros</span>
          </Button>
        </div>
      </div>

      {/* Navegación inferior fija */}
      <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 shadow-2xl">
        <div className="grid grid-cols-3 max-w-md mx-auto">
          <button 
            onClick={() => setShowStore(true)}
            className="flex flex-col items-center py-4 px-6 hover:bg-green-50 transition-colors border-r border-gray-200"
          >
            <ShoppingBag className="w-6 h-6 text-green-600 mb-1" />
            <span className="text-gray-700">Tienda</span>
          </button>
          <button 
            onClick={() => setShowScanner(true)}
            className="flex flex-col items-center py-4 px-6 bg-green-50 border-r border-gray-200"
          >
            <div className="relative">
              <div className="absolute inset-0 bg-green-600 rounded-full blur-md opacity-30"></div>
              <div className="relative bg-gradient-to-br from-green-600 to-emerald-600 p-3 rounded-full shadow-lg -mt-8">
                <QrCode className="w-7 h-7 text-white" />
              </div>
            </div>
            <span className="text-green-600 mt-2">Escanear</span>
          </button>
          <button 
            onClick={() => setShowRanking(true)}
            className="flex flex-col items-center py-4 px-6 hover:bg-green-50 transition-colors"
          >
            <Trophy className="w-6 h-6 text-green-600 mb-1" />
            <span className="text-gray-700">Ranking</span>
          </button>
        </div>
      </div>

      {/* Modal de resultado */}
      {showResult && (
        <ScanResult 
          qrData={scannedData}
          onClose={() => setShowResult(false)}
          onContinue={handleContinueAfterScan}
        />
      )}

      {/* Panel de notificaciones */}
      {showNotifications && (
        <Notifications onClose={() => setShowNotifications(false)} />
      )}
    </div>
  );
}