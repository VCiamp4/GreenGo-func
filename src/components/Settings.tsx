import { ArrowLeft, Moon, Sun, Bell, BellOff, Volume2, VolumeX, Globe, Trash2, LogOut, Shield, HelpCircle, Mail, ChevronRight } from 'lucide-react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { useState } from 'react';

interface SettingsProps {
  userName: string;
  userEmail: string;
  onBack: () => void;
}

export function Settings({ userName, userEmail, onBack }: SettingsProps) {
  const [darkMode, setDarkMode] = useState(false);
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [language, setLanguage] = useState('Español');

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
    // Aquí podrías aplicar el tema oscuro a toda la app
  };

  const toggleNotifications = () => {
    setNotificationsEnabled(!notificationsEnabled);
  };

  const toggleSound = () => {
    setSoundEnabled(!soundEnabled);
  };

  return (
    <div className={`min-h-screen pb-20 transition-colors duration-300 ${
      darkMode 
        ? 'bg-gradient-to-b from-gray-900 via-gray-800 to-gray-900' 
        : 'bg-gradient-to-b from-blue-50 via-indigo-50 to-white'
    }`}>
      {/* Header */}
      <div className={`pt-12 pb-8 px-6 rounded-b-3xl shadow-lg mb-6 ${
        darkMode 
          ? 'bg-gradient-to-r from-gray-800 to-gray-700' 
          : 'bg-gradient-to-r from-blue-600 to-indigo-600'
      }`}>
        <div className="flex items-center mb-6">
          <button 
            onClick={onBack}
            className="bg-white/20 backdrop-blur-sm p-3 rounded-full hover:bg-white/30 transition-all shadow-md mr-4"
          >
            <ArrowLeft className="w-6 h-6 text-white" />
          </button>
          <div className="flex-1">
            <h1 className="text-white mb-1">Configuración</h1>
            <p className={darkMode ? 'text-gray-300' : 'text-blue-100'}>Personaliza tu experiencia</p>
          </div>
        </div>
      </div>

      <div className="px-6 space-y-6">
        {/* Información de la cuenta */}
        <Card className={`border-0 shadow-md ${darkMode ? 'bg-gray-800' : 'bg-white'}`}>
          <div className="p-5">
            <h3 className={`mb-4 ${darkMode ? 'text-white' : 'text-gray-800'}`}>Cuenta</h3>
            <div className="space-y-3">
              <div>
                <p className={`mb-1 ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>Nombre</p>
                <p className={darkMode ? 'text-white' : 'text-gray-800'}>{userName}</p>
              </div>
              <div>
                <p className={`mb-1 ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>Email</p>
                <p className={darkMode ? 'text-white' : 'text-gray-800'}>{userEmail}</p>
              </div>
            </div>
          </div>
        </Card>

        {/* Preferencias de Apariencia y Notificaciones */}
        <Card className={`border-0 shadow-md ${darkMode ? 'bg-gray-800' : 'bg-white'}`}>
          <div className="p-5">
            <h3 className={`mb-4 ${darkMode ? 'text-white' : 'text-gray-800'}`}>Apariencia y Sonido</h3>
            <div className="space-y-4">
              {/* Modo Oscuro */}
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  {darkMode ? (
                    <div className="bg-indigo-100 p-2 rounded-lg">
                      <Moon className="w-5 h-5 text-indigo-600" />
                    </div>
                  ) : (
                    <div className="bg-yellow-100 p-2 rounded-lg">
                      <Sun className="w-5 h-5 text-yellow-600" />
                    </div>
                  )}
                  <div>
                    <p className={darkMode ? 'text-white' : 'text-gray-800'}>Modo Oscuro</p>
                    <p className={`${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
                      {darkMode ? 'Activado' : 'Desactivado'}
                    </p>
                  </div>
                </div>
                <button
                  onClick={toggleDarkMode}
                  className={`relative w-14 h-8 rounded-full transition-colors ${
                    darkMode ? 'bg-indigo-600' : 'bg-gray-300'
                  }`}
                >
                  <div
                    className={`absolute top-1 w-6 h-6 bg-white rounded-full shadow-md transition-transform ${
                      darkMode ? 'translate-x-7' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>

              {/* Notificaciones */}
              <div className="flex items-center justify-between pt-4 border-t border-gray-200">
                <div className="flex items-center gap-3">
                  {notificationsEnabled ? (
                    <div className="bg-green-100 p-2 rounded-lg">
                      <Bell className="w-5 h-5 text-green-600" />
                    </div>
                  ) : (
                    <div className="bg-red-100 p-2 rounded-lg">
                      <BellOff className="w-5 h-5 text-red-600" />
                    </div>
                  )}
                  <div>
                    <p className={darkMode ? 'text-white' : 'text-gray-800'}>Notificaciones</p>
                    <p className={`${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
                      {notificationsEnabled ? 'Activadas' : 'Desactivadas'}
                    </p>
                  </div>
                </div>
                <button
                  onClick={toggleNotifications}
                  className={`relative w-14 h-8 rounded-full transition-colors ${
                    notificationsEnabled ? 'bg-green-600' : 'bg-gray-300'
                  }`}
                >
                  <div
                    className={`absolute top-1 w-6 h-6 bg-white rounded-full shadow-md transition-transform ${
                      notificationsEnabled ? 'translate-x-7' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>

              {/* Sonido */}
              <div className="flex items-center justify-between pt-4 border-t border-gray-200">
                <div className="flex items-center gap-3">
                  {soundEnabled ? (
                    <div className="bg-blue-100 p-2 rounded-lg">
                      <Volume2 className="w-5 h-5 text-blue-600" />
                    </div>
                  ) : (
                    <div className="bg-gray-200 p-2 rounded-lg">
                      <VolumeX className="w-5 h-5 text-gray-600" />
                    </div>
                  )}
                  <div>
                    <p className={darkMode ? 'text-white' : 'text-gray-800'}>Sonidos</p>
                    <p className={`${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
                      {soundEnabled ? 'Activados' : 'Desactivados'}
                    </p>
                  </div>
                </div>
                <button
                  onClick={toggleSound}
                  className={`relative w-14 h-8 rounded-full transition-colors ${
                    soundEnabled ? 'bg-blue-600' : 'bg-gray-300'
                  }`}
                >
                  <div
                    className={`absolute top-1 w-6 h-6 bg-white rounded-full shadow-md transition-transform ${
                      soundEnabled ? 'translate-x-7' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>
            </div>
          </div>
        </Card>

        {/* Idioma */}
        <Card className={`border-0 shadow-md ${darkMode ? 'bg-gray-800' : 'bg-white'}`}>
          <div className="p-5">
            <h3 className={`mb-4 ${darkMode ? 'text-white' : 'text-gray-800'}`}>Idioma</h3>
            <button className="w-full flex items-center justify-between p-3 rounded-lg hover:bg-gray-100 transition-colors">
              <div className="flex items-center gap-3">
                <div className="bg-purple-100 p-2 rounded-lg">
                  <Globe className="w-5 h-5 text-purple-600" />
                </div>
                <div className="text-left">
                  <p className={darkMode ? 'text-white' : 'text-gray-800'}>Idioma de la aplicación</p>
                  <p className={darkMode ? 'text-gray-400' : 'text-gray-600'}>{language}</p>
                </div>
              </div>
              <ChevronRight className={`w-5 h-5 ${darkMode ? 'text-gray-400' : 'text-gray-400'}`} />
            </button>
          </div>
        </Card>

        {/* Ayuda y Soporte */}
        <Card className={`border-0 shadow-md ${darkMode ? 'bg-gray-800' : 'bg-white'}`}>
          <div className="p-5">
            <h3 className={`mb-4 ${darkMode ? 'text-white' : 'text-gray-800'}`}>Ayuda y Soporte</h3>
            <div className="space-y-2">
              <button className="w-full flex items-center justify-between p-3 rounded-lg hover:bg-gray-100 transition-colors">
                <div className="flex items-center gap-3">
                  <div className="bg-blue-100 p-2 rounded-lg">
                    <HelpCircle className="w-5 h-5 text-blue-600" />
                  </div>
                  <p className={darkMode ? 'text-white' : 'text-gray-800'}>Centro de Ayuda</p>
                </div>
                <ChevronRight className={`w-5 h-5 ${darkMode ? 'text-gray-400' : 'text-gray-400'}`} />
              </button>
              
              <button className="w-full flex items-center justify-between p-3 rounded-lg hover:bg-gray-100 transition-colors">
                <div className="flex items-center gap-3">
                  <div className="bg-green-100 p-2 rounded-lg">
                    <Mail className="w-5 h-5 text-green-600" />
                  </div>
                  <p className={darkMode ? 'text-white' : 'text-gray-800'}>Contactar Soporte</p>
                </div>
                <ChevronRight className={`w-5 h-5 ${darkMode ? 'text-gray-400' : 'text-gray-400'}`} />
              </button>

              <button className="w-full flex items-center justify-between p-3 rounded-lg hover:bg-gray-100 transition-colors">
                <div className="flex items-center gap-3">
                  <div className="bg-amber-100 p-2 rounded-lg">
                    <Shield className="w-5 h-5 text-amber-600" />
                  </div>
                  <p className={darkMode ? 'text-white' : 'text-gray-800'}>Privacidad y Seguridad</p>
                </div>
                <ChevronRight className={`w-5 h-5 ${darkMode ? 'text-gray-400' : 'text-gray-400'}`} />
              </button>
            </div>
          </div>
        </Card>

        {/* Acciones de cuenta */}
        <Card className={`border-0 shadow-md ${darkMode ? 'bg-gray-800' : 'bg-white'}`}>
          <div className="p-5 space-y-3">
            <Button
              variant="outline"
              className="w-full justify-start text-red-600 border-red-200 hover:bg-red-50"
            >
              <Trash2 className="w-5 h-5 mr-3" />
              Eliminar Datos de la App
            </Button>
            
            <Button
              variant="outline"
              className="w-full justify-start text-gray-700 hover:bg-gray-100"
            >
              <LogOut className="w-5 h-5 mr-3" />
              Cerrar Sesión
            </Button>
          </div>
        </Card>

        {/* Versión */}
        <div className="text-center pb-4">
          <p className={`${darkMode ? 'text-gray-500' : 'text-gray-400'}`}>
            EcoRecicla v1.0.0
          </p>
          <p className={`${darkMode ? 'text-gray-600' : 'text-gray-500'}`}>
            © 2025 Todos los derechos reservados
          </p>
        </div>
      </div>
    </div>
  );
}
