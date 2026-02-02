import { X, Trophy, Gift, Zap, Medal, CheckCircle2, Clock, Flame } from 'lucide-react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';

interface NotificationsProps {
  onClose: () => void;
}

interface Notification {
  id: string;
  type: 'achievement' | 'reward' | 'streak' | 'challenge';
  title: string;
  message: string;
  time: string;
  read: boolean;
  icon: React.ElementType;
  iconColor: string;
  iconBg: string;
}

export function Notifications({ onClose }: NotificationsProps) {
  const notifications: Notification[] = [
    {
      id: '1',
      type: 'achievement',
      title: '¡Nuevo Logro Desbloqueado!',
      message: 'Has completado "Reciclador Principiante" - 5 elementos reciclados',
      time: 'Hace 2 horas',
      read: false,
      icon: Medal,
      iconColor: 'text-yellow-600',
      iconBg: 'bg-yellow-100'
    },
    {
      id: '2',
      type: 'streak',
      title: '¡Racha de 5 días!',
      message: 'Mantén tu racha activa. ¡Solo 2 días más para una recompensa especial!',
      time: 'Hace 5 horas',
      read: false,
      icon: Flame,
      iconColor: 'text-orange-600',
      iconBg: 'bg-orange-100'
    },
    {
      id: '3',
      type: 'reward',
      title: 'Recompensa Diaria',
      message: 'Has ganado 50 monedas por tu racha activa',
      time: 'Hoy',
      read: true,
      icon: Gift,
      iconColor: 'text-purple-600',
      iconBg: 'bg-purple-100'
    },
    {
      id: '4',
      type: 'challenge',
      title: 'Nuevo Desafío Disponible',
      message: 'Recicla 10 botellas de plástico esta semana y gana 200 puntos extra',
      time: 'Ayer',
      read: true,
      icon: Zap,
      iconColor: 'text-blue-600',
      iconBg: 'bg-blue-100'
    },
    {
      id: '5',
      type: 'achievement',
      title: '¡Subiste al Top 15%!',
      message: 'Tu posición en el ranking ha mejorado. ¡Sigue así!',
      time: 'Hace 2 días',
      read: true,
      icon: Trophy,
      iconColor: 'text-green-600',
      iconBg: 'bg-green-100'
    }
  ];

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <div className="fixed inset-0 bg-black/50 z-50 animate-in fade-in duration-200">
      <div className="absolute right-0 top-0 h-full w-full max-w-md bg-white shadow-2xl animate-in slide-in-from-right duration-300">
        {/* Header */}
        <div className="bg-gradient-to-r from-green-600 to-emerald-600 p-6 pb-8">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-white mb-1">Notificaciones</h2>
              {unreadCount > 0 && (
                <p className="text-green-100">{unreadCount} nuevas</p>
              )}
            </div>
            <button
              onClick={onClose}
              className="bg-white/20 backdrop-blur-sm p-2 rounded-full hover:bg-white/30 transition-all"
            >
              <X className="w-6 h-6 text-white" />
            </button>
          </div>
        </div>

        {/* Lista de notificaciones */}
        <div className="overflow-y-auto h-[calc(100%-120px)] p-4 space-y-3">
          {notifications.map((notification) => {
            const Icon = notification.icon;
            return (
              <Card
                key={notification.id}
                className={`border-0 shadow-md transition-all hover:shadow-lg ${
                  !notification.read ? 'bg-green-50/50 border-l-4 border-l-green-600' : 'bg-white'
                }`}
              >
                <div className="p-4">
                  <div className="flex items-start gap-4">
                    {/* Icono */}
                    <div className={`flex-shrink-0 w-12 h-12 rounded-full ${notification.iconBg} flex items-center justify-center`}>
                      <Icon className={`w-6 h-6 ${notification.iconColor}`} />
                    </div>

                    {/* Contenido */}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2 mb-1">
                        <h3 className="text-gray-800">{notification.title}</h3>
                        {!notification.read && (
                          <div className="w-2 h-2 bg-green-600 rounded-full flex-shrink-0 mt-1"></div>
                        )}
                      </div>
                      <p className="text-gray-600 mb-2">
                        {notification.message}
                      </p>
                      <div className="flex items-center gap-2">
                        <Clock className="w-3 h-3 text-gray-400" />
                        <span className="text-gray-400">{notification.time}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </Card>
            );
          })}

          {/* Mensaje cuando no hay más notificaciones */}
          <div className="text-center py-8">
            <CheckCircle2 className="w-12 h-12 text-gray-300 mx-auto mb-3" />
            <p className="text-gray-400">Has visto todas las notificaciones</p>
          </div>
        </div>

        {/* Footer */}
        <div className="absolute bottom-0 left-0 right-0 p-4 bg-gray-50 border-t border-gray-200">
          <Button
            onClick={onClose}
            variant="outline"
            className="w-full"
          >
            Cerrar
          </Button>
        </div>
      </div>
    </div>
  );
}
