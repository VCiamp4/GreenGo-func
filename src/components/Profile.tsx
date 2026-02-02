import { ArrowLeft, Camera, Edit2, Mail, MapPin, Calendar, Trophy, Zap, Star, Award, Save, X, User, Phone } from 'lucide-react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { Avatar, AvatarFallback } from './ui/avatar';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { useState } from 'react';

interface ProfileProps {
  userName: string;
  userLevel: number;
  totalPoints: number;
  streakDays: number;
  onBack: () => void;
}

export function Profile({ userName, userLevel, totalPoints, streakDays, onBack }: ProfileProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [editedName, setEditedName] = useState(userName);
  const [editedEmail, setEditedEmail] = useState('usuario@email.com');
  const [editedPhone, setEditedPhone] = useState('+34 600 123 456');
  const [editedLocation, setEditedLocation] = useState('Madrid, España');
  const [editedBio, setEditedBio] = useState('Amante del reciclaje y del medio ambiente 🌱');

  const handleSave = () => {
    // Aquí guardarías los cambios
    setIsEditing(false);
  };

  const handleCancel = () => {
    // Restaurar valores originales
    setEditedName(userName);
    setEditedEmail('usuario@email.com');
    setEditedPhone('+34 600 123 456');
    setEditedLocation('Madrid, España');
    setEditedBio('Amante del reciclaje y del medio ambiente 🌱');
    setIsEditing(false);
  };

  const stats = [
    { icon: Trophy, label: 'Logros', value: '18/25', color: 'from-yellow-400 to-amber-500' },
    { icon: Zap, label: 'Racha', value: `${streakDays} días`, color: 'from-orange-400 to-red-500' },
    { icon: Star, label: 'Nivel', value: userLevel, color: 'from-emerald-400 to-teal-500' },
    { icon: Award, label: 'Ranking', value: '#8', color: 'from-purple-400 to-pink-500' }
  ];

  const achievements = [
    { name: 'Eco Guerrero', description: 'Recicló 100 items', icon: '🏆', unlocked: true },
    { name: 'Racha de Fuego', description: '30 días consecutivos', icon: '🔥', unlocked: true },
    { name: 'Maestro Verde', description: 'Alcanzó nivel 10', icon: '🌟', unlocked: false },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-teal-50 to-cyan-50 pb-24 animate-fade-in">
      {/* Header con imagen de perfil grande */}
      <div className="relative bg-gradient-to-r from-emerald-600 via-green-600 to-teal-600 pt-12 pb-32 px-6 rounded-b-[2.5rem] shadow-2xl overflow-hidden">
        {/* Efectos de fondo animados */}
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 transform rotate-12 animate-float">
            <User className="w-32 h-32" />
          </div>
          <div className="absolute bottom-10 left-10 transform -rotate-12 animate-pulse-glow">
            <Trophy className="w-24 h-24" />
          </div>
        </div>

        {/* Efectos de luz */}
        <div className="absolute inset-0 overflow-hidden">
          <div className="absolute top-0 right-0 w-64 h-64 bg-teal-400/30 rounded-full blur-3xl animate-float"></div>
          <div className="absolute bottom-0 left-0 w-48 h-48 bg-emerald-400/20 rounded-full blur-2xl animate-pulse-glow"></div>
        </div>

        <div className="relative z-10">
          {/* Botón de volver y editar */}
          <div className="flex items-center justify-between mb-8">
            <button 
              onClick={onBack}
              className="p-3 bg-white/20 backdrop-blur-md rounded-2xl hover:bg-white/30 transition-all shadow-lg border border-white/30 group"
            >
              <ArrowLeft className="w-6 h-6 text-white group-hover:-translate-x-1 transition-transform" />
            </button>
            {!isEditing ? (
              <button 
                onClick={() => setIsEditing(true)}
                className="p-3 bg-white/20 backdrop-blur-md rounded-2xl hover:bg-white/30 transition-all shadow-lg border border-white/30 group"
              >
                <Edit2 className="w-6 h-6 text-white group-hover:rotate-12 transition-transform" />
              </button>
            ) : (
              <div className="flex gap-2">
                <button 
                  onClick={handleCancel}
                  className="p-3 bg-red-500/80 backdrop-blur-md rounded-2xl hover:bg-red-600/80 transition-all shadow-lg border border-white/30"
                >
                  <X className="w-6 h-6 text-white" />
                </button>
                <button 
                  onClick={handleSave}
                  className="p-3 bg-emerald-500/80 backdrop-blur-md rounded-2xl hover:bg-emerald-600/80 transition-all shadow-lg border border-white/30"
                >
                  <Save className="w-6 h-6 text-white" />
                </button>
              </div>
            )}
          </div>

          {/* Avatar grande centrado */}
          <div className="text-center">
            <div className="relative inline-block mb-4">
              {/* Anillo animado */}
              <div className="absolute -inset-2 bg-gradient-to-r from-yellow-400 via-amber-500 to-yellow-400 rounded-full blur-lg opacity-60 animate-pulse"></div>
              
              {/* Avatar */}
              <div className="relative">
                <Avatar className="h-32 w-32 border-4 border-white shadow-2xl ring-4 ring-white/30">
                  <AvatarFallback className="bg-gradient-to-br from-emerald-600 to-teal-700 text-white text-5xl">
                    {editedName.charAt(0).toUpperCase()}
                  </AvatarFallback>
                </Avatar>
                
                {/* Botón cambiar foto */}
                <button className="absolute bottom-0 right-0 bg-gradient-to-br from-emerald-500 to-teal-600 rounded-full p-3 shadow-xl border-3 border-white hover:scale-110 transition-transform">
                  <Camera className="w-5 h-5 text-white" />
                </button>

                {/* Badge de nivel */}
                <div className="absolute -top-2 -right-2 bg-gradient-to-br from-yellow-400 to-amber-500 rounded-full px-3 py-1 shadow-xl border-2 border-white">
                  <span className="text-white">Lvl {userLevel}</span>
                </div>
              </div>
            </div>

            {/* Nombre y descripción */}
            <div className="text-white">
              <h1 className="mb-2 drop-shadow-lg">{editedName}</h1>
              <p className="text-emerald-100 drop-shadow-md mb-4">{editedBio}</p>
              
              {/* Badges */}
              <div className="flex items-center justify-center gap-2 flex-wrap">
                <Badge className="bg-white/25 backdrop-blur-sm text-white border-0 shadow-lg">
                  <Trophy className="w-4 h-4 mr-1.5" />
                  {totalPoints.toLocaleString()} pts
                </Badge>
                <Badge className="bg-white/25 backdrop-blur-sm text-white border-0 shadow-lg">
                  <Award className="w-4 h-4 mr-1.5" />
                  Top 15%
                </Badge>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Estadísticas */}
      <div className="px-6 -mt-20 mb-6 relative z-10 animate-slide-up">
        <div className="grid grid-cols-2 gap-3">
          {stats.map((stat, index) => {
            const Icon = stat.icon;
            return (
              <Card key={index} className="bg-white border-0 shadow-xl rounded-3xl p-5 hover:shadow-2xl transition-all">
                <div className="flex flex-col items-center text-center">
                  <div className={`w-14 h-14 rounded-2xl bg-gradient-to-br ${stat.color} flex items-center justify-center shadow-lg mb-3`}>
                    <Icon className="w-7 h-7 text-white" />
                  </div>
                  <p className="text-gray-600 mb-1">{stat.label}</p>
                  <div className="text-gray-800">{stat.value}</div>
                </div>
              </Card>
            );
          })}
        </div>
      </div>

      {/* Formulario de información */}
      <div className="px-6 space-y-4">
        {isEditing ? (
          <Card className="bg-white border-0 shadow-xl rounded-3xl p-6">
            <h3 className="text-gray-800 mb-6">Editar Información</h3>
            
            <div className="space-y-5">
              {/* Nombre */}
              <div>
                <Label className="text-gray-700 mb-2 block">Nombre completo</Label>
                <div className="relative">
                  <User className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                  <Input
                    value={editedName}
                    onChange={(e) => setEditedName(e.target.value)}
                    className="pl-12 h-12 rounded-xl border-gray-200 focus:border-emerald-500"
                  />
                </div>
              </div>

              {/* Email */}
              <div>
                <Label className="text-gray-700 mb-2 block">Email</Label>
                <div className="relative">
                  <Mail className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                  <Input
                    type="email"
                    value={editedEmail}
                    onChange={(e) => setEditedEmail(e.target.value)}
                    className="pl-12 h-12 rounded-xl border-gray-200 focus:border-emerald-500"
                  />
                </div>
              </div>

              {/* Teléfono */}
              <div>
                <Label className="text-gray-700 mb-2 block">Teléfono</Label>
                <div className="relative">
                  <Phone className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                  <Input
                    type="tel"
                    value={editedPhone}
                    onChange={(e) => setEditedPhone(e.target.value)}
                    className="pl-12 h-12 rounded-xl border-gray-200 focus:border-emerald-500"
                  />
                </div>
              </div>

              {/* Ubicación */}
              <div>
                <Label className="text-gray-700 mb-2 block">Ubicación</Label>
                <div className="relative">
                  <MapPin className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                  <Input
                    value={editedLocation}
                    onChange={(e) => setEditedLocation(e.target.value)}
                    className="pl-12 h-12 rounded-xl border-gray-200 focus:border-emerald-500"
                  />
                </div>
              </div>

              {/* Biografía */}
              <div>
                <Label className="text-gray-700 mb-2 block">Biografía</Label>
                <textarea
                  value={editedBio}
                  onChange={(e) => setEditedBio(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-emerald-500 focus:ring-2 focus:ring-emerald-500/20 resize-none"
                  rows={3}
                  placeholder="Cuéntanos sobre ti..."
                />
              </div>
            </div>
          </Card>
        ) : (
          <Card className="bg-white border-0 shadow-xl rounded-3xl p-6">
            <h3 className="text-gray-800 mb-6">Información Personal</h3>
            
            <div className="space-y-4">
              {/* Email */}
              <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                <div className="bg-emerald-100 rounded-xl p-3">
                  <Mail className="w-5 h-5 text-emerald-600" />
                </div>
                <div className="flex-1">
                  <p className="text-gray-600">Email</p>
                  <p className="text-gray-800">{editedEmail}</p>
                </div>
              </div>

              {/* Teléfono */}
              <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                <div className="bg-blue-100 rounded-xl p-3">
                  <Phone className="w-5 h-5 text-blue-600" />
                </div>
                <div className="flex-1">
                  <p className="text-gray-600">Teléfono</p>
                  <p className="text-gray-800">{editedPhone}</p>
                </div>
              </div>

              {/* Ubicación */}
              <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                <div className="bg-purple-100 rounded-xl p-3">
                  <MapPin className="w-5 h-5 text-purple-600" />
                </div>
                <div className="flex-1">
                  <p className="text-gray-600">Ubicación</p>
                  <p className="text-gray-800">{editedLocation}</p>
                </div>
              </div>

              {/* Fecha de registro */}
              <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                <div className="bg-orange-100 rounded-xl p-3">
                  <Calendar className="w-5 h-5 text-orange-600" />
                </div>
                <div className="flex-1">
                  <p className="text-gray-600">Miembro desde</p>
                  <p className="text-gray-800">Enero 2024</p>
                </div>
              </div>
            </div>
          </Card>
        )}

        {/* Logros destacados */}
        <Card className="bg-white border-0 shadow-xl rounded-3xl p-6">
          <h3 className="text-gray-800 mb-6">Logros Destacados</h3>
          
          <div className="space-y-3">
            {achievements.map((achievement, index) => (
              <div 
                key={index}
                className={`flex items-center gap-4 p-4 rounded-xl transition-all ${
                  achievement.unlocked 
                    ? 'bg-gradient-to-r from-emerald-50 to-teal-50 border border-emerald-200' 
                    : 'bg-gray-50 border border-gray-200 opacity-60'
                }`}
              >
                <div className="text-4xl">{achievement.icon}</div>
                <div className="flex-1">
                  <p className={`${achievement.unlocked ? 'text-gray-800' : 'text-gray-500'}`}>
                    {achievement.name}
                  </p>
                  <p className="text-gray-600">{achievement.description}</p>
                </div>
                {achievement.unlocked && (
                  <div className="bg-emerald-500 rounded-full p-2">
                    <Trophy className="w-5 h-5 text-white" />
                  </div>
                )}
              </div>
            ))}
          </div>

          <Button 
            className="w-full mt-4 bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-700 hover:to-teal-700 rounded-xl shadow-lg"
          >
            Ver Todos los Logros
          </Button>
        </Card>

        {/* Estadísticas detalladas */}
        <Card className="bg-gradient-to-br from-emerald-100 via-green-50 to-teal-100 border-0 shadow-xl rounded-3xl p-6">
          <h3 className="text-gray-800 mb-6">Impacto Ambiental</h3>
          
          <div className="grid grid-cols-2 gap-4">
            <div className="bg-white rounded-2xl p-4 text-center">
              <div className="text-3xl mb-2">🌳</div>
              <p className="text-gray-800 mb-1">45 kg</p>
              <p className="text-gray-600">CO₂ Reducido</p>
            </div>
            <div className="bg-white rounded-2xl p-4 text-center">
              <div className="text-3xl mb-2">♻️</div>
              <p className="text-gray-800 mb-1">156</p>
              <p className="text-gray-600">Items Reciclados</p>
            </div>
            <div className="bg-white rounded-2xl p-4 text-center">
              <div className="text-3xl mb-2">💧</div>
              <p className="text-gray-800 mb-1">890 L</p>
              <p className="text-gray-600">Agua Ahorrada</p>
            </div>
            <div className="bg-white rounded-2xl p-4 text-center">
              <div className="text-3xl mb-2">⚡</div>
              <p className="text-gray-800 mb-1">34 kWh</p>
              <p className="text-gray-600">Energía Ahorrada</p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}
