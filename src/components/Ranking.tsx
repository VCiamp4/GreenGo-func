import { Trophy, Medal, Crown, TrendingUp, Users, ArrowLeft, Zap, UserPlus } from 'lucide-react';
import { Card } from './ui/card';
import { Avatar, AvatarFallback } from './ui/avatar';
import { Badge } from './ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './ui/tabs';

interface RankingProps {
  userName: string;
  onBack: () => void;
}

export function Ranking({ userName, onBack }: RankingProps) {
  // Datos simulados del ranking global
  const globalRanking = [
    { position: 1, name: 'María García', points: 2850, streak: 45, level: 12, avatar: 'MG' },
    { position: 2, name: 'Carlos Ruiz', points: 2640, streak: 38, level: 11, avatar: 'CR' },
    { position: 3, name: 'Ana López', points: 2420, streak: 32, level: 10, avatar: 'AL' },
    { position: 4, name: 'Pedro Sánchez', points: 2180, streak: 28, level: 9, avatar: 'PS' },
    { position: 5, name: 'Laura Martín', points: 1950, streak: 24, level: 8, avatar: 'LM' },
    { position: 6, name: 'José Torres', points: 1820, streak: 22, level: 8, avatar: 'JT' },
    { position: 7, name: 'Carmen Díaz', points: 1650, streak: 19, level: 7, avatar: 'CD' },
    { position: 8, name: userName, points: 1250, streak: 12, level: 5, avatar: userName.charAt(0).toUpperCase(), isCurrentUser: true },
    { position: 9, name: 'Francisco Gil', points: 1120, streak: 15, level: 6, avatar: 'FG' },
    { position: 10, name: 'Isabel Ramos', points: 980, streak: 11, level: 5, avatar: 'IR' },
  ];

  // Datos del ranking semanal
  const weeklyRanking = [
    { position: 1, name: 'Ana López', points: 420, change: '+2', avatar: 'AL' },
    { position: 2, name: userName, points: 380, change: '+5', avatar: userName.charAt(0).toUpperCase(), isCurrentUser: true },
    { position: 3, name: 'Carlos Ruiz', points: 350, change: '-1', avatar: 'CR' },
    { position: 4, name: 'María García', points: 320, change: '0', avatar: 'MG' },
    { position: 5, name: 'Laura Martín', points: 290, change: '+3', avatar: 'LM' },
  ];

  // Datos del ranking de amigos (contactos)
  const friendsRanking = [
    { position: 1, name: 'Carlos Ruiz', points: 2640, streak: 38, level: 11, avatar: 'CR', phone: '+34 600 123 456' },
    { position: 2, name: userName, points: 1250, streak: 12, level: 5, avatar: userName.charAt(0).toUpperCase(), isCurrentUser: true, phone: 'Tu número' },
    { position: 3, name: 'Laura Martín', points: 1950, streak: 24, level: 8, avatar: 'LM', phone: '+34 600 789 012' },
    { position: 4, name: 'José Torres', points: 1820, streak: 22, level: 8, avatar: 'JT', phone: '+34 600 345 678' },
    { position: 5, name: 'Francisco Gil', points: 1120, streak: 15, level: 6, avatar: 'FG', phone: '+34 600 901 234' },
    { position: 6, name: 'Isabel Ramos', points: 980, streak: 11, level: 5, avatar: 'IR', phone: '+34 600 567 890' },
  ];

  const getMedalIcon = (position: number) => {
    if (position === 1) return <Crown className="w-6 h-6 text-yellow-500" />;
    if (position === 2) return <Medal className="w-6 h-6 text-gray-400" />;
    if (position === 3) return <Medal className="w-6 h-6 text-amber-700" />;
    return null;
  };

  const getPositionColor = (position: number) => {
    if (position === 1) return 'from-yellow-400 to-amber-500';
    if (position === 2) return 'from-gray-300 to-gray-400';
    if (position === 3) return 'from-amber-600 to-amber-700';
    return 'from-green-500 to-emerald-600';
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-emerald-50 via-teal-50 to-cyan-50 pb-24 animate-fade-in">
      {/* Header mejorado */}
      <div className="relative bg-gradient-to-r from-emerald-600 via-green-600 to-teal-600 pt-12 pb-24 px-6 rounded-b-[2.5rem] shadow-2xl overflow-hidden">
        {/* Patrón de fondo animado */}
        <div className="absolute inset-0 opacity-20">
          <div className="absolute top-10 right-10 transform rotate-12 animate-float">
            <Trophy className="w-32 h-32" />
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
          <button 
            onClick={onBack}
            className="mb-6 p-3 bg-white/20 backdrop-blur-md rounded-2xl hover:bg-white/30 transition-all shadow-lg border border-white/30 group"
          >
            <ArrowLeft className="w-6 h-6 text-white group-hover:-translate-x-1 transition-transform" />
          </button>

          <div className="text-center">
            <div className="relative inline-block mb-4">
              <div className="absolute inset-0 bg-yellow-400 rounded-full blur-2xl opacity-40 animate-pulse"></div>
              <Trophy className="relative w-16 h-16 text-yellow-300 drop-shadow-2xl animate-float" />
            </div>
            <h1 className="text-white mb-2 drop-shadow-lg">Ranking de Recicladores</h1>
            <p className="text-emerald-100 drop-shadow-md">Compite con otros usuarios</p>
          </div>
        </div>
      </div>

      {/* Card de posición del usuario mejorada */}
      <div className="px-6 -mt-16 mb-6 animate-slide-up">
        <Card className="relative bg-gradient-to-br from-emerald-600 via-green-600 to-teal-600 border-0 shadow-2xl rounded-3xl p-6 text-white overflow-hidden">
          {/* Efectos de fondo */}
          <div className="absolute inset-0 overflow-hidden">
            <div className="absolute top-0 right-0 w-32 h-32 bg-white/10 rounded-full blur-2xl"></div>
            <div className="absolute bottom-0 left-0 w-24 h-24 bg-white/5 rounded-full blur-xl"></div>
          </div>

          <div className="relative z-10">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-4">
                <div className="relative">
                  <div className="absolute -inset-1 bg-gradient-to-r from-yellow-400 to-amber-500 rounded-full blur opacity-60"></div>
                  <Avatar className="relative h-16 w-16 border-3 border-white shadow-2xl ring-4 ring-white/30">
                    <AvatarFallback className="bg-gradient-to-br from-emerald-700 to-teal-800 text-white text-xl">
                      {userName.charAt(0).toUpperCase()}
                    </AvatarFallback>
                  </Avatar>
                </div>
                <div>
                  <h3 className="text-white mb-2 drop-shadow-lg">Tu Posición</h3>
                  <div className="flex items-center gap-2">
                    <Badge className="bg-white/25 backdrop-blur-sm text-white border-0 shadow-lg">
                      Nivel 5
                    </Badge>
                    <div className="flex items-center gap-1">
                      <Zap className="w-4 h-4 text-yellow-300" />
                      <span className="text-sm">12 días</span>
                    </div>
                  </div>
                </div>
              </div>
              <div className="text-right">
                <div className="text-5xl mb-1 drop-shadow-lg">#8</div>
                <p className="text-emerald-100 drop-shadow-md">1,250 pts</p>
              </div>
            </div>
            
            <div className="flex items-center gap-2 text-emerald-100 bg-white/10 backdrop-blur-sm rounded-xl p-3">
              <TrendingUp className="w-5 h-5" />
              <span>+5 posiciones esta semana</span>
            </div>
          </div>
        </Card>
      </div>

      {/* Tabs */}
      <div className="px-6">
        <Tabs defaultValue="global" className="w-full">
          <TabsList className="grid w-full grid-cols-3 mb-6">
            <TabsTrigger value="global">Global</TabsTrigger>
            <TabsTrigger value="semanal">Semanal</TabsTrigger>
            <TabsTrigger value="amigos">Amigos</TabsTrigger>
          </TabsList>

          {/* Ranking Global */}
          <TabsContent value="global" className="space-y-3">
            {/* Top 3 destacado */}
            <div className="grid grid-cols-3 gap-3 mb-6">
              {/* Segundo lugar */}
              <div className="text-center pt-8">
                <div className="relative inline-block mb-2">
                  <Avatar className="h-14 w-14 border-2 border-gray-300 shadow-lg">
                    <AvatarFallback className="bg-gray-400 text-white">
                      {globalRanking[1].avatar}
                    </AvatarFallback>
                  </Avatar>
                  <div className="absolute -top-2 -right-2 bg-gradient-to-br from-gray-300 to-gray-400 rounded-full p-1.5 shadow-md">
                    <span className="text-white text-xs">2</span>
                  </div>
                </div>
                <p className="text-gray-700 text-sm mb-1 truncate">{globalRanking[1].name.split(' ')[0]}</p>
                <p className="text-gray-600 text-xs">{globalRanking[1].points} pts</p>
              </div>

              {/* Primer lugar */}
              <div className="text-center">
                <div className="relative inline-block mb-2">
                  <Avatar className="h-20 w-20 border-4 border-yellow-400 shadow-2xl">
                    <AvatarFallback className="bg-gradient-to-br from-yellow-400 to-amber-500 text-white text-xl">
                      {globalRanking[0].avatar}
                    </AvatarFallback>
                  </Avatar>
                  <div className="absolute -top-3 left-1/2 transform -translate-x-1/2">
                    <Crown className="w-8 h-8 text-yellow-400 drop-shadow-lg" />
                  </div>
                </div>
                <p className="text-gray-800 mb-1 truncate">{globalRanking[0].name.split(' ')[0]}</p>
                <p className="text-green-600">{globalRanking[0].points} pts</p>
              </div>

              {/* Tercer lugar */}
              <div className="text-center pt-8">
                <div className="relative inline-block mb-2">
                  <Avatar className="h-14 w-14 border-2 border-amber-700 shadow-lg">
                    <AvatarFallback className="bg-amber-700 text-white">
                      {globalRanking[2].avatar}
                    </AvatarFallback>
                  </Avatar>
                  <div className="absolute -top-2 -right-2 bg-gradient-to-br from-amber-600 to-amber-700 rounded-full p-1.5 shadow-md">
                    <span className="text-white text-xs">3</span>
                  </div>
                </div>
                <p className="text-gray-700 text-sm mb-1 truncate">{globalRanking[2].name.split(' ')[0]}</p>
                <p className="text-gray-600 text-xs">{globalRanking[2].points} pts</p>
              </div>
            </div>

            {/* Lista del ranking */}
            {globalRanking.slice(3).map((user) => (
              <Card 
                key={user.position}
                className={`p-4 ${user.isCurrentUser ? 'bg-gradient-to-r from-green-50 to-emerald-50 border-2 border-green-500' : 'bg-white'} shadow-md hover:shadow-lg transition-all`}
              >
                <div className="flex items-center gap-4">
                  {/* Posición */}
                  <div className={`flex items-center justify-center w-10 h-10 rounded-full ${user.isCurrentUser ? 'bg-gradient-to-br from-green-500 to-emerald-600' : 'bg-gray-100'}`}>
                    <span className={`${user.isCurrentUser ? 'text-white' : 'text-gray-700'}`}>
                      {user.position}
                    </span>
                  </div>

                  {/* Avatar */}
                  <Avatar className={`h-12 w-12 ${user.isCurrentUser ? 'border-2 border-green-500' : ''}`}>
                    <AvatarFallback className={user.isCurrentUser ? 'bg-green-600 text-white' : 'bg-gray-200'}>
                      {user.avatar}
                    </AvatarFallback>
                  </Avatar>

                  {/* Info */}
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <p className={user.isCurrentUser ? 'text-green-700' : 'text-gray-800'}>
                        {user.name}
                      </p>
                      {user.isCurrentUser && (
                        <Badge className="bg-green-600 hover:bg-green-600 text-white text-xs">Tú</Badge>
                      )}
                    </div>
                    <div className="flex items-center gap-3 text-xs text-gray-600 mt-1">
                      <span>Nivel {user.level}</span>
                      <span className="flex items-center gap-1">
                        <Zap className="w-3 h-3 text-orange-500" />
                        {user.streak} días
                      </span>
                    </div>
                  </div>

                  {/* Puntos */}
                  <div className="text-right">
                    <p className={user.isCurrentUser ? 'text-green-600' : 'text-gray-700'}>
                      {user.points}
                    </p>
                    <p className="text-xs text-gray-500">puntos</p>
                  </div>
                </div>
              </Card>
            ))}
          </TabsContent>

          {/* Ranking Semanal */}
          <TabsContent value="semanal" className="space-y-3">
            <div className="flex items-center gap-2 text-gray-600 mb-4 p-4 bg-blue-50 rounded-xl">
              <Users className="w-5 h-5 text-blue-600" />
              <span>Puntos ganados en los últimos 7 días</span>
            </div>

            {weeklyRanking.map((user) => (
              <Card 
                key={user.position}
                className={`p-4 ${user.isCurrentUser ? 'bg-gradient-to-r from-green-50 to-emerald-50 border-2 border-green-500' : 'bg-white'} shadow-md`}
              >
                <div className="flex items-center gap-4">
                  {/* Medalla o posición */}
                  <div className="w-10 flex items-center justify-center">
                    {getMedalIcon(user.position) || (
                      <div className={`flex items-center justify-center w-10 h-10 rounded-full ${user.isCurrentUser ? 'bg-gradient-to-br from-green-500 to-emerald-600' : 'bg-gray-100'}`}>
                        <span className={user.isCurrentUser ? 'text-white' : 'text-gray-700'}>
                          {user.position}
                        </span>
                      </div>
                    )}
                  </div>

                  {/* Avatar */}
                  <Avatar className={`h-12 w-12 ${user.isCurrentUser ? 'border-2 border-green-500' : ''}`}>
                    <AvatarFallback className={user.isCurrentUser ? 'bg-green-600 text-white' : 'bg-gray-200'}>
                      {user.avatar}
                    </AvatarFallback>
                  </Avatar>

                  {/* Info */}
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <p className={user.isCurrentUser ? 'text-green-700' : 'text-gray-800'}>
                        {user.name}
                      </p>
                      {user.isCurrentUser && (
                        <Badge className="bg-green-600 hover:bg-green-600 text-white text-xs">Tú</Badge>
                      )}
                    </div>
                    <div className="flex items-center gap-2 text-xs mt-1">
                      <Badge 
                        className={`${
                          user.change.startsWith('+') 
                            ? 'bg-green-100 text-green-700' 
                            : user.change.startsWith('-')
                            ? 'bg-red-100 text-red-700'
                            : 'bg-gray-100 text-gray-700'
                        } border-0`}
                      >
                        {user.change.startsWith('+') && <TrendingUp className="w-3 h-3 mr-1" />}
                        {user.change}
                      </Badge>
                    </div>
                  </div>

                  {/* Puntos */}
                  <div className="text-right">
                    <p className={user.isCurrentUser ? 'text-green-600' : 'text-gray-700'}>
                      {user.points}
                    </p>
                    <p className="text-xs text-gray-500">pts</p>
                  </div>
                </div>
              </Card>
            ))}
          </TabsContent>

          {/* Ranking de Amigos */}
          <TabsContent value="amigos" className="space-y-3">
            <div className="flex items-center justify-between gap-2 mb-4 p-4 bg-purple-50 rounded-xl">
              <div className="flex items-center gap-2 text-gray-600">
                <Users className="w-5 h-5 text-purple-600" />
                <span>Ranking de amigos</span>
              </div>
              <button className="bg-purple-600 hover:bg-purple-700 text-white px-3 py-1.5 rounded-lg flex items-center gap-2">
                <UserPlus className="w-4 h-4" />
                <span className="text-sm">Invitar</span>
              </button>
            </div>

            {friendsRanking.map((user) => (
              <Card 
                key={user.position}
                className={`p-4 ${user.isCurrentUser ? 'bg-gradient-to-r from-green-50 to-emerald-50 border-2 border-green-500' : 'bg-white'} shadow-md`}
              >
                <div className="flex items-center gap-4">
                  {/* Medalla o posición */}
                  <div className="w-10 flex items-center justify-center">
                    {getMedalIcon(user.position) || (
                      <div className={`flex items-center justify-center w-10 h-10 rounded-full ${user.isCurrentUser ? 'bg-gradient-to-br from-green-500 to-emerald-600' : 'bg-gray-100'}`}>
                        <span className={user.isCurrentUser ? 'text-white' : 'text-gray-700'}>
                          {user.position}
                        </span>
                      </div>
                    )}
                  </div>

                  {/* Avatar */}
                  <Avatar className={`h-12 w-12 ${user.isCurrentUser ? 'border-2 border-green-500' : ''}`}>
                    <AvatarFallback className={user.isCurrentUser ? 'bg-green-600 text-white' : 'bg-gray-200'}>
                      {user.avatar}
                    </AvatarFallback>
                  </Avatar>

                  {/* Info */}
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <p className={user.isCurrentUser ? 'text-green-700' : 'text-gray-800'}>
                        {user.name}
                      </p>
                      {user.isCurrentUser && (
                        <Badge className="bg-green-600 hover:bg-green-600 text-white text-xs">Tú</Badge>
                      )}
                    </div>
                    <div className="flex items-center gap-3 text-xs text-gray-600 mt-1">
                      <span>Nivel {user.level}</span>
                      <span className="flex items-center gap-1">
                        <Zap className="w-3 h-3 text-orange-500" />
                        {user.streak} días
                      </span>
                    </div>
                  </div>

                  {/* Puntos */}
                  <div className="text-right">
                    <p className={user.isCurrentUser ? 'text-green-600' : 'text-gray-700'}>
                      {user.points}
                    </p>
                    <p className="text-xs text-gray-500">puntos</p>
                  </div>
                </div>
              </Card>
            ))}
            
            {/* Botón para agregar más amigos */}
            <Card className="p-6 bg-gradient-to-br from-purple-50 to-indigo-50 border-2 border-dashed border-purple-300 text-center">
              <UserPlus className="w-12 h-12 text-purple-400 mx-auto mb-3" />
              <h3 className="text-gray-800 mb-2">Invita a tus amigos</h3>
              <p className="text-gray-600 mb-4">
                Sincroniza tus contactos o comparte tu código para competir con más amigos
              </p>
              <button className="bg-purple-600 hover:bg-purple-700 text-white px-6 py-2 rounded-lg">
                Sincronizar Contactos
              </button>
            </Card>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
}